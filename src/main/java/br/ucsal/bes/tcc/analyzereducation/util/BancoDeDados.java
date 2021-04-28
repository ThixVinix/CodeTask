package br.ucsal.bes.tcc.analyzereducation.util;

import java.util.ArrayList;
import java.util.List;

import br.ucsal.bes.tcc.analyzereducation.model.Atividade;
import br.ucsal.bes.tcc.analyzereducation.model.Autor;
import br.ucsal.bes.tcc.analyzereducation.model.Tarefa;
import br.ucsal.bes.tcc.analyzereducation.model.Teste;

public class BancoDeDados {

	private static List<Tarefa> tarefas = new ArrayList<>();
	
	static {
		Tarefa tarefa1 = new Tarefa();
		tarefa1.setId(0L);
		tarefa1.setTitulo("Soma entre dois números");
		tarefa1.setDescricao("Faça a soma entre 452 + 267");
		Teste teste1 = new Teste();
		teste1.setId(1L);
		teste1.setEntradas("2\n2\n4\n4");
		teste1.setSaidas("4\n8");
		Teste teste2 = new Teste();
		teste2.setId(2L);
		teste2.setEntradas("3\n3\n5\n5");
		teste2.setSaidas("6\n10");
		Teste teste3 = new Teste();
		teste3.setId(3L);
		teste3.setEntradas("0.5\n0.5\n7\n5");
		teste3.setSaidas("1\n12");
		Teste teste4 = new Teste();
		teste4.setId(4L);
		teste4.setEntradas("4\n4\n1\n1");
		teste4.setSaidas("8\n2");
		tarefa1.getTestes().add(teste1);
		tarefa1.getTestes().add(teste2);
		tarefa1.getTestes().add(teste3);
		tarefa1.getTestes().add(teste4);
		
		Tarefa tarefa2 = new Tarefa();
		tarefa2.setId(1L);
		tarefa2.setTitulo("Subtração entre dois números");
		tarefa2.setDescricao("Faça a subtração entre 452 - 267");
		
		tarefas.add(tarefa1);
		tarefas.add(tarefa2);
		
	}
	
	public static List<Tarefa> obterTarefas() {
		return tarefas;
	}
	
	public static Tarefa obterTarefa(Long id) {
		
		return tarefas.get(id.intValue());
		
//			Teste teste1 = new Teste();
//			teste1.setId(1L);
//			teste1.setEntradas("2\n2\n4\n4");
//			teste1.setSaidas("4\n8");
//			Teste teste2 = new Teste();
//			teste2.setId(2L);
//			teste2.setEntradas("3\n3\n5\n5");
//			teste2.setSaidas("6\n10");
//			Tarefa tarefa1 = new Tarefa();
//			tarefa1.setId(1L);
//			tarefa1.setTitulo("Soma entre dois números");
//			tarefa1.setDescricao("Faça a soma entre 452 + 267");
//			tarefa1.getTestes().add(teste1);
//			tarefa1.getTestes().add(teste2);
//			// tarefa1.setResultadoEsperado("719");
//			tarefa1.setConcluido(false);
//			Tarefa tarefa2 = new Tarefa();
//			tarefa2.setId(2L);
//			tarefa2.setTitulo("Subtração entre dois números");
//			tarefa2.setDescricao("Faça a subtração entre 452 - 267");
//			// tarefa2.setResultadoEsperado("185");
//			tarefa2.setConcluido(false);
//			Tarefa tarefa3 = new Tarefa();
//			tarefa3.setId(3L);
//			tarefa3.setTitulo("Multiplicação entre dois números");
//			tarefa3.setDescricao("Faça a multiplicação entre 5 x 5");
//			// tarefa3.setResultadoEsperado("25");
//			tarefa3.setConcluido(false);
//			Tarefa tarefa4 = new Tarefa();
//			tarefa4.setId(4L);
//			tarefa4.setTitulo("Divisão entre dois números");
//			tarefa4.setDescricao("Faça a divisão entre 25 / 5");
//			// tarefa4.setResultadoEsperado("5");
//			tarefa4.setConcluido(false);
//			Atividade atividade1 = new Atividade();
//			atividade1.setId(1L);
//			atividade1.setTitulo("Soma e Subtração");
//			atividade1.setDescricao("Realize as operações de soma e subtração");
//			List<Tarefa> tarefas1 = new ArrayList<>();
//			tarefas1.add(tarefa1);
//			tarefas1.add(tarefa2);
//			atividade1.setTarefas(tarefas1);
//			atividade1.setConcluido(false);
//			Atividade atividade2 = new Atividade();
//			atividade2.setId(2L);
//			atividade2.setTitulo("Multiplicação e Divisão");
//			atividade2.setDescricao("Realize as operações de multiplicação e divisão");
//			List<Tarefa> tarefas2 = new ArrayList<>();
//			tarefas2.add(tarefa3);
//			tarefas2.add(tarefa4);
//			atividade2.setTarefas(tarefas2);
//			atividade2.setConcluido(false);
//			setAutor(new Autor());
//			getAutor().setId(1L);
//			getAutor().setNome("João");
//			List<Atividade> atividades = new ArrayList<>();
//			atividades.add(atividade1);
//			atividades.add(atividade2);
//			getAutor().setAtividades(atividades);
		
		
	}
	
}