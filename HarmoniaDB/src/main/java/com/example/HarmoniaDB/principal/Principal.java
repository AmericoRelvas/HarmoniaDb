package com.example.HarmoniaDB.principal;

import com.example.HarmoniaDB.model.Artista;
import com.example.HarmoniaDB.model.Musica;
import com.example.HarmoniaDB.model.TipoArtista;
import com.example.HarmoniaDB.repository.ArtistaRepository;
import com.example.HarmoniaDB.service.consultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository repositorio;
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }
    public void exibeMenu() {

        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    *** Harmonia Músicas *** \s
                    1 - Cadastrar artistas
                    2 - Cadastrar musicas
                    3 - Listar musicas
                    4 - Buscar musicas por artista
                    5 - Pesquisar dados sobre um artista
                      
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtista();
                    break;
                case 2:
                    cadastrarMusica();
                    break;
                case 3:
                    listarMusica();
                    break;
                case 4:
                    buscarMusicaPorArtista();
                    break;
                case 5:
                    pesquisarDadosSobreUmArtista();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void pesquisarDadosSobreUmArtista() {
        System.out.println("Pesquisar dados sobre qual artista? ");
        var nome = leitura.nextLine();
        var resposta = consultaChatGPT.obterInformacao(nome);
        System.out.println(resposta.trim());
    }

    private void buscarMusicaPorArtista() {
        System.out.println("Buscar músicas de que artista? ");
        var nome = leitura.nextLine();
        List<Musica> musicas = repositorio.buscarMusicasPorArtista(nome);
        musicas.forEach(System.out::println);
    }

    private void listarMusica() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusica() {
        System.out.println("Cadastrar música de que artista? ");
        var nome = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);
        if (artista.isPresent()) {
            System.out.println("Informe o título da música: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("Artista não encontrado");
        }
    }

    private void cadastrarArtista() {
        var cadastrarNovo = "S";

        while (cadastrarNovo.equalsIgnoreCase("s")) {
        System.out.println("Informe o nome desse artista: ");
        var nome = leitura.nextLine();
        System.out.println("Informe o tipo desse artista: (solo, dupla ou banda)");
        var tipo = leitura.nextLine();
        TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
        Artista artista = new Artista(nome, tipoArtista);
        repositorio.save(artista);
            System.out.println("Cadastrar novo artista? (S/N)");
            cadastrarNovo = leitura.nextLine();
        }
    }
}
