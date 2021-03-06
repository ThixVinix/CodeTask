package br.ucsal.bes.tcc.analyzereducation.analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.ucsal.bes.tcc.analyzereducation.abstracts.AbstractArquivoMetrica;
import br.ucsal.bes.tcc.analyzereducation.enums.ValidacaoArquivoEnum;
import br.ucsal.bes.tcc.analyzereducation.model.ArquivoMetrica;
import br.ucsal.bes.tcc.analyzereducation.model.Premissa;
import br.ucsal.bes.tcc.analyzereducation.model.Resultado;
import br.ucsal.bes.tcc.analyzereducation.model.ResultadoTeste;
import br.ucsal.bes.tcc.analyzereducation.model.Tarefa;
import br.ucsal.bes.tcc.analyzereducation.model.Teste;
import br.ucsal.bes.tcc.analyzereducation.util.Constante;
import br.ucsal.bes.tcc.analyzereducation.util.DirectoryUtil;
import br.ucsal.bes.tcc.analyzereducation.util.Util;

public class JavaCode extends AbstractArquivoMetrica {

	private static final Logger LOGGER = LogManager.getLogger(JavaCode.class.getName());

	private static final String MESSAGE_STARTED_ANALYZE = "Iniciando análise das métricas do arquivo: \"%s\"...";

	private static final String DIRETORIO_NOME_ARQUIVO = "C:\\Users\\Public\\Documents\\DefaultName";

	private static final String DIRETORIO = "C:\\Users\\Public\\Documents";

	private File fileJava;

	private Map<ArquivoMetrica, Boolean> filesJava;

	private StringBuilder output;

	private Resultado result;

	public JavaCode() {
		super();
		setFilesJava(new HashMap<>());
		setOutput(new StringBuilder());
	}

	public ValidacaoArquivoEnum validarDiretorio(String diretorioString) {
		LOGGER.info("Validando campo do diretório...");

		if (Util.isNullOrEmpty(diretorioString)) {
			LOGGER.info(Constante.PREENCHIMENTO_OBRIGATORIO_DIRETORIO);
			return ValidacaoArquivoEnum.PREENCHIMENTO_OBRIGATORIO_DIRETORIO;
		}

		Path caminho = DirectoryUtil.obterPathPeloDiretorioString(diretorioString.trim());

		if (Util.isNullOrEmpty(caminho)) {
			LOGGER.error(Constante.ERRO_CONVERSAO_DIRETORIO);
			return ValidacaoArquivoEnum.ERRO_CONVERSAO_DIRETORIO;
		}
		if (!DirectoryUtil.validarDiretorioCompleto(caminho)) {
			LOGGER.info(Constante.DIRETORIO_NAO_ENCONTRADO);
			return ValidacaoArquivoEnum.DIRETORIO_NAO_ENCONTRADO;
		}

		if (!DirectoryUtil.isJavaFile(caminho.toFile())) {
			LOGGER.info(Constante.TIPO_ARQUIVO_INCORRETO);
			return ValidacaoArquivoEnum.TIPO_ARQUIVO_INCORRETO;
		}

		LOGGER.info("Validação do diretório efetuada com sucesso.");
		return ValidacaoArquivoEnum.SUCESSO;
	}

	public ValidacaoArquivoEnum validarCamposEntidadesDeusas(String qtdMinMetodoDeusString,
			String qtdMinClasseDeusString) {
		LOGGER.info("Validando campos das entidades deusas...");

		if (Util.isNullOrEmpty(qtdMinMetodoDeusString)) {
			LOGGER.info(Constante.PREENCHIMENTO_OBRIGATORIO_METODO_DEUS);
			return ValidacaoArquivoEnum.PREENCHIMENTO_OBRIGATORIO_METODO_DEUS;
		}

		if (Util.isNullOrEmpty(qtdMinClasseDeusString)) {
			LOGGER.info(Constante.PREENCHIMENTO_OBRIGATORIO_CLASSE_DEUS);
			return ValidacaoArquivoEnum.PREENCHIMENTO_OBRIGATORIO_CLASSE_DEUS;
		}

		if (!Util.isNumerico(qtdMinMetodoDeusString.trim())) {
			LOGGER.info(Constante.PREENCHIMENTO_INCORRETO_METODO_DEUS);
			return ValidacaoArquivoEnum.PREENCHIMENTO_INCORRETO_METODO_DEUS;
		}

		if (!Util.isNumerico(qtdMinClasseDeusString.trim())) {
			LOGGER.info(Constante.PREENCHIMENTO_INCORRETO_CLASSE_DEUS);
			return ValidacaoArquivoEnum.PREENCHIMENTO_INCORRETO_CLASSE_DEUS;
		}

		LOGGER.info("Validação das entidades deusas efetuada com sucesso.");
		return ValidacaoArquivoEnum.SUCESSO;
	}

	public Optional<ArquivoMetrica> iniciarAnalise(File arquivo, List<Premissa> listaFiltros, Integer limiteMinMetodoDeus,
			Integer limiteMinClasseDeus) throws IOException {

		ArquivoMetrica arqMetric = null;

		this.setArquivoPesquisado(new ArquivoMetrica());

		if (Util.isNullOrEmpty(limiteMinMetodoDeus) || limiteMinMetodoDeus < Constante.NUMBER_ZERO_INT)
			limiteMinMetodoDeus = Constante.NUMBER_ZERO_INT;

		if (Util.isNullOrEmpty(limiteMinClasseDeus) || limiteMinClasseDeus < Constante.NUMBER_ZERO_INT)
			limiteMinClasseDeus = Constante.NUMBER_ZERO_INT;

		if (Util.isNullOrEmpty(arquivo)) {
			LOGGER.warn("Arquivo para análise foi passado como nulo.");
			salvarMetricas();
			return Optional.ofNullable(arqMetric);
		}

		BufferedReader arquivoReader = DirectoryUtil.lerArquivo(arquivo);

		if (Util.isNullOrEmpty(arquivoReader)) {
			LOGGER.warn("Não é possível realizar a leitura linha à linha do arquivo.");
			salvarMetricas();
			return Optional.ofNullable(arqMetric);
		}

		String line;

		var message = String.format(MESSAGE_STARTED_ANALYZE, arquivo.getName());
		LOGGER.info(message);

		while ((line = arquivoReader.readLine()) != null) {
			analisarLinhaAtual(line, arquivo);
		}

		arquivoReader.close();
		LOGGER.info("Análise das métricas finalizada.");

		LOGGER.info("Iniciando análise de métodos deuses...");
		qtdMetodoDeus = verificarEntidadesDeusas(limiteMinMetodoDeus, true);
		LOGGER.info("Análise de métodos deuses finalizada.");

		LOGGER.info("Iniciando análise de classes deusas...");
		qtdClasseDeus = verificarEntidadesDeusas(limiteMinClasseDeus, false);
		LOGGER.info("Análise de classes deusas finalizada.");

		if (listaFiltros != null && !listaFiltros.isEmpty()) {
			for (Premissa filtro : listaFiltros) {
				var mFilter = String.format("Iniciando verificação do filtro \"%s\"...", filtro.getNomeFiltro());
				LOGGER.info(mFilter);
				result.getMapResultFilter().put(filtro, verificarUtilizacaoFiltro(filtro));
				LOGGER.info("Verificação efetuada.");
			}
		}

		salvarMetricas();

		arqMetric = super.getArquivoPesquisado();

		resetarMetricasProvisorias();

		return Optional.of(arqMetric);
	}

	public Resultado executarCodigo(String conteudo, Tarefa tarefa) {

		result = new Resultado();

		setFileJava(DirectoryUtil.criarArquivoComExtensao(DIRETORIO_NOME_ARQUIVO, Constante.ARQUIVO_TIPO_JAVA));

		DirectoryUtil.adicionarConteudoArquivo(getFileJava(), conteudo);

		// PASTA ATUAL
//		System.out.println(FileSystems.getDefault().getPath("").toAbsolutePath().toString());

		output = new StringBuilder();

		Optional<ArquivoMetrica> arqMetric;

		try {
			arqMetric = iniciarAnalise(getFileJava(), tarefa.getFiltros(), Constante.QTD_MIN_LINHAS_METODO_DEUS,
					Constante.QTD_MIN_LINHAS_CLASSE_DEUS);

			if (arqMetric.isPresent())
				result.getArquivosMetrica().add(arqMetric.get());

		} catch (IOException e) {
			LOGGER.catching(e.getCause());
			// output.append(e.getMessage() + Constante.QUEBRA_LINHA);
		}
		acessarArquivosAnalisados();

		ArquivoMetrica arquivoPrincipal = null;

		arquivoPrincipal = obterArquivoPrincipal();

		if (arquivoPrincipal != null) {
			String nomeArquivo = arquivoPrincipal.obterNomeArquivo();
			
			// First stage - Compile
			output.append(compilar(DIRETORIO, "javac", nomeArquivo));

			try {
				if (tarefa.getTestes() != null && !tarefa.getTestes().isEmpty()) {

					for (Teste teste : tarefa.getTestes()) {

						// Second stage - Execute
						ResultadoTeste resultadoComTeste = executarComTeste(DIRETORIO, "java", nomeArquivo,
								teste.getEntradas(), teste.getSaidas());

						result.getMapResultTest().put(teste, resultadoComTeste);
						result.getSaidasObtidas().add(output.toString());
						output = new StringBuilder();
					}

				} else {
					executarSemTeste(DIRETORIO, "java", nomeArquivo);
					result.getSaidasObtidas().add(output.toString());
					output = new StringBuilder();
				}

			} catch (IOException e) {
				output.append(e.getMessage() + Constante.QUEBRA_LINHA);
			}

		} else {
			LOGGER.warn("Não foi possível executar o código");
			output = new StringBuilder();
		}

		return result;
	}

	private ArquivoMetrica obterArquivoPrincipal() {
		ArquivoMetrica arquivoPrincipal = null;

		for (Map.Entry<ArquivoMetrica, Boolean> file : filesJava.entrySet()) {
			boolean isMain = file.getValue();
			if (isMain) {
				arquivoPrincipal = file.getKey();
				break;
			}
		}
		return arquivoPrincipal;
	}

	private void acessarArquivosAnalisados() {
		for (Map.Entry<ArquivoMetrica, File> mapArquivoAlvo : arquivosAnalisados.entrySet()) {
			if ((mapArquivoAlvo.getKey().getArquivo() != null && mapArquivoAlvo.getKey().getArquivo().exists())) {
				if (!mapArquivoAlvo.getValue().exists()) {
					renomearArquivoAlvo(mapArquivoAlvo);
				} else {
					var message = String.format(Constante.ARQUIVO_EXISTENTE, mapArquivoAlvo.getValue());
					LOGGER.warn(message);
					DirectoryUtil.deletarArquivo(mapArquivoAlvo.getValue().toPath());
					renomearArquivoAlvo(mapArquivoAlvo);
				}

				mapArquivoAlvo.getKey().setCaminho(mapArquivoAlvo.getValue().toPath());
				mapArquivoAlvo.getKey().setArquivo(mapArquivoAlvo.getValue());
			}

			verificarArquivosPrincipais(mapArquivoAlvo);
		}
	}

	private void renomearArquivoAlvo(Map.Entry<ArquivoMetrica, File> mapArquivoAlvo) {
		String resultado = DirectoryUtil.renomearArquivo(mapArquivoAlvo.getKey().getArquivo(),
				mapArquivoAlvo.getValue());
		LOGGER.info(resultado);
	}

	private void verificarArquivosPrincipais(Entry<ArquivoMetrica, File> mapArquivoAlvo) {

		var contemMetodoMain = false;
		String conteudo = mapArquivoAlvo.getKey().getConteudoCompactado();
		var st = new StringTokenizer(conteudo, Constante.QUEBRA_LINHA);
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			contemMetodoMain = verificarMetodoMain(line);
			if (contemMetodoMain) {
				filesJava.put(getArquivoPesquisado(), true);
				break;
			}
		}

		if (!contemMetodoMain) {
			filesJava.put(getArquivoPesquisado(), false);
		}
	}

	private boolean verificarMetodoMain(String line) {

		try {
			var padrao = Pattern.compile(Constante.REGEX_METODO_MAIN);
			var encontrador = padrao.matcher(line.trim());
			while (encontrador.find()) {
				return true;
			}

		} catch (PatternSyntaxException e) {
			LOGGER.catching(e);
		}

		return false;
	}

	public static String compilar(String path, String comando, String arquivo) {

		var sb = new StringBuilder();
		var builder = new ProcessBuilder(comando, arquivo);

		builder.directory(new File(path));
		Process process;
		try {
			process = builder.start();

			var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			var error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + Constante.QUEBRA_LINHA);
			}

			while ((line = error.readLine()) != null) {
				sb.append(line + Constante.QUEBRA_LINHA);
			}

			// int exitCode = process.waitFor();
			// System.out.println("\nExited with error code : " + exitCode);

		} catch (IOException e) {
			sb.append(e.getMessage() + Constante.QUEBRA_LINHA);
		}

		if (Util.isNullOrEmpty(sb.toString())) {
			return Constante.VAZIO;
		} else {
			return sb.toString();
		}

	}

	public ResultadoTeste executarComTeste(String path, String comando, String arquivo, String entrada, String saida)
			throws IOException {

		var builder = new ProcessBuilder(comando, arquivo);
		builder.directory(new File(path));
		builder.redirectErrorStream(true);
		var process = builder.start();

		var stdin = process.getOutputStream();
		var stdout = process.getInputStream();

//		var reader = new BufferedReader(new InputStreamReader(stdout));
//		var error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		var writer = new BufferedWriter(new OutputStreamWriter(stdin));
		var isFirstLine = true;
		var st = new StringTokenizer(entrada, Constante.QUEBRA_LINHA);

		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			if (isFirstLine) {
				writer.write(line);
				isFirstLine = false;
			} else {
				writer.write(Constante.QUEBRA_LINHA + line);
			}
		}

		writer.flush();
		writer.close();

		var scanner = new Scanner(stdout);
		var resposta = new StringBuilder();
		isFirstLine = true;
		while (scanner.hasNextLine()) {
			if (isFirstLine) {
				resposta.append(scanner.nextLine());
				isFirstLine = false;
			} else {
				resposta.append(Constante.QUEBRA_LINHA + scanner.nextLine());
			}
		}

		scanner.close();

		output.append(resposta.toString());

		boolean isCorrect = resposta.toString().equals(saida);

		var rTest = new ResultadoTeste();

		rTest.setSaidaObtida(resposta.toString());
		rTest.setCorrect(isCorrect);

		return rTest;
	}

	public void executarSemTeste(String path, String comando, String arquivo) throws IOException {

		var sb = new StringBuilder();

		var builder = new ProcessBuilder(comando, arquivo);

		builder.directory(new File(path));
		Process process;

		process = builder.start();

		var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		var error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line + Constante.QUEBRA_LINHA);
		}

		while ((line = error.readLine()) != null) {
			sb.append(line + Constante.QUEBRA_LINHA);
		}

		output.append(sb.toString());

	}

	public File getFileJava() {
		return fileJava;
	}

	public void setFileJava(File fileJava) {
		this.fileJava = fileJava;
	}

	public Map<ArquivoMetrica, Boolean> getFilesJava() {
		return filesJava;
	}

	public void setFilesJava(Map<ArquivoMetrica, Boolean> filesJava) {
		this.filesJava = filesJava;
	}

	public StringBuilder getOutput() {
		return output;
	}

	public void setOutput(StringBuilder output) {
		this.output = output;
	}

}
