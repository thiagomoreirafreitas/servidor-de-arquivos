import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args)
	{
		// Criando cliente
		Cliente cli = new Cliente();

		// Aguardar conexao de cliente para transferia
		cli.recebeServidor();
	}
	
		public void recebeServidor()
		{
		
		//Diretorio onde será salvo o arquivo enviado pelo servidor
		String diretorio = "C:\\Users\\Thiago\\Desktop\\Cliente\\";
		
		//Inicialização 
		Socket conexao = null;
		
		InputStream socketIn = null;
		OutputStream socketOut = null;
		
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		
		ObjectInputStream input = null;
		ObjectOutputStream out = null;
		
		
		//Recebe menu do servidor
		String menu="";
		//Recebe a opção desejada do cliente em relação ao menu
		int opcao;
		//Variavel que recebe a senha digitada pelo cliente (Senha para realizar envio do arquivo para o servidor)
		String senha="";
		//Recebe o nome do arquivo que o cliente deseja receber do servidor
		String mensagem="";
		//Scanner para ler opção do menu
		Scanner tc = new Scanner(System.in);
		//Scanner para ler nome do arquivo para recebimento
		Scanner tc2 = new Scanner(System.in);
		//Scanner para ler senha
		Scanner tc3 = new Scanner(System.in);
		
		//Vetor afile recebe a lista de arquivos do servidor
		File afile[];

		try {
			
			// Criando conexão com o servidor
			conexao = new Socket("127.0.0.1", 12345);
			System.out.println("CLIENTE: Conexão realizada");
			socketIn = conexao.getInputStream();
			
			//Configuracao do objeto de saida
			out = new ObjectOutputStream(conexao.getOutputStream());
			//Configuração de objetos de entrada 
			input = new ObjectInputStream(conexao.getInputStream());
			
			//Imprimi menu na tela do cliente
			menu=(String) input.readObject();
			System.out.println(menu);
			
			//recebe opcao do cliente
			opcao=tc.nextInt();
			
			//tratamento de erro de escolha de opção
			while(opcao!=1 && opcao!=2)
			{
				System.out.println("CLIENTE: ERRO:OPCAO INVALIDA");
				System.out.println(menu);
				opcao=tc.nextInt();
			}
			//Fim tratamento de erro
			
			//Envia para o servidor a opção do cliente
			out.writeObject(opcao);
			out.flush();
			
			if(opcao==1) //(1 - Receber arquivo do Servidor)
			{
			//Imprimir contéudo do diretório do servidor
			afile = (File[]) input.readObject();
			System.out.println("__________________________________________________________");
			System.out.println("CLIENTE:Arquivos no servidor");
			int i = 0; 	
			for (int j = afile.length; i < j; i++)
			{ 
				File arquivos = afile[i]; 
				System.out.println(arquivos.getName()); 
			} 
			System.out.println("__________________________________________________________");
			
			//Entrada do usuário para realizar download do servidor
	        System.out.println("CLIENTE:Digite o nome do arquivo para download:");  
	        mensagem = tc2.nextLine();
	        
	        //Tratamento de erro digitar nome de arquivo que não está presente no diretorio
	        i=0;
	        //variavel auxiliar para tratamento de erro se for igual a zero não deixa o programa prosseguir
	        int aux=0;
	   
	        for (int j = afile.length; i < j; i++)
			{ 
				File arquivos = afile[i]; 
				if(arquivos.getName().equals(mensagem))
				{
					aux++;
				}
				
			} 
			while(aux==0)
			{
				//Entrada do usuário para realizar download do servidor
		        System.out.println("Cliente: ERRO!!! NÃO EXISTE ESTE ARQUIVO NO DIRETORIO");  
		        
				//Entrada do usuário para realizar download do servidor
		        System.out.println("CLIENTE:Digite o nome do arquivo para download:(Nome deve ser digitado igual ao listado)");  
		        mensagem = tc2.nextLine();
				
				i=0;
				 for (int j = afile.length; i < j; i++)
					{ 
						File arquivos = afile[i]; 
						if(arquivos.getName().equals(mensagem))
						{
							aux++;
						}
					} 
			}
			//FIM TRATAMENTO DE ERRO
			
			
	        //Envia para o servidor o nome do arquivo desejado
			out.writeObject(mensagem);
	        
	        
			// Prepara variaveis para transferencia
			byte[] buffer = new byte[1024];
			int bytesLidos;

			
			// Arquivo recebido pelo cliente
			fileOut = new FileOutputStream(new File(diretorio+mensagem));
			System.out.println("CLIENTE: Arquivo Salvo em:"+diretorio);
			
			// Copia conteudo do canal
			System.out.println("CLIENTE: Recebendo arquivo...");
			while ((bytesLidos = socketIn.read(buffer)) != -1) 
			{
				fileOut.write(buffer, 0, bytesLidos);
				fileOut.flush();
			}
			
			System.out.println("CLIENTE: Arquivo recebido!");
		    
			}
			//FIM DA OPÇÃO 1
			
			if(opcao==2)//(2 - Enviar arquivo para o Servidor)
			{
				
			//enviar arquivo
			
			//Requisição da senha do cliente
			System.out.println("CLIENTE:Informe a senha");
			senha=tc3.nextLine();
			//Envia senha para o servidor para verificar a autenticidade
			out.writeObject(senha);
			
			int respSenha;
			respSenha=(int) input.readObject();
			while(respSenha !=1)
			{
				out.flush();
				System.out.println("CLIENTE:ERRO: SENHA INVALIDA");
				System.out.println("CLIENTE:Informe a senha");
				senha=tc3.nextLine();  
				out.writeObject(senha);
				respSenha=(int) input.readObject();
			}
		
			//Imprimir contéudo do diretório do cliente
			
			//Rotina para listar conteudo do diretorio no cliente
			listarDiretorio v = new  listarDiretorio();
			File[] afile2;
			afile2=v.ver(diretorio);
		
		
			System.out.println("__________________________________________________________");
			System.out.println("CLIENTE:Arquivos no diretorio Cliente para o envio para o servidor");
			int i = 0; 	
			for (int j = afile2.length; i < j; i++)
			{ 
				File arquivos = afile2[i]; 
				System.out.println(arquivos.getName()); 
			} 
			System.out.println("__________________________________________________________");
			
			//Entrada do usuário para realizar download do servidor
	        System.out.println("CLIENTE:Digite o nome do arquivo para eviar:");  
	        mensagem = tc2.nextLine();
	        
	        //Tratamento de erro digitar nome de arquivo que não está presente no diretorio
	        i=0;
	        //variavel auxiliar para tratamento de erro se for igual a zero não deixa o programa prosseguir
	        int aux=0;
	   
	        for (int j = afile2.length; i < j; i++)
			{ 
				File arquivos = afile2[i]; 
				if(arquivos.getName().equals(mensagem))
				{
					aux++;
				}
				
			} 
			while(aux==0)
			{
				//Entrada do usuário para realizar download do servidor
		        System.out.println("CLIENTE: ERRO!!! NÃO EXISTE ESTE ARQUIVO NO DIRETORIO");  
		        
				//Entrada do usuário para realizar download do servidor
		        System.out.println("CLIENTE:Digite o nome do arquivo para enviar:(Nome deve ser digitado igual ao listado)");  
		        mensagem = tc2.nextLine();
				
				i=0;
				 for (int j = afile2.length; i < j; i++)
					{ 
						File arquivos = afile2[i]; 
						if(arquivos.getName().equals(mensagem))
						{
							aux++;
						}
					} 
			}
			//FIM TRATAMENTO DE ERRO
			
			
	        //Envia para o servidor o nome do arquivo desejado para o envio
			out.writeObject(mensagem);
			
			
			// Criando buffer para realizar a leitura
			byte[] buffer = new byte[1024];
			int bytesLidos;
						  
			// Arquivo que sera transferido para o servidor
			File f1 = new File(diretorio+mensagem);
			fileIn = new FileInputStream(f1);
			System.out.println("CLIENTE:Lendo arquivo...");
			
			// Criando canal de transferencia
			socketOut = conexao.getOutputStream();

			// Lendo arquivo  e enviado para o canal de transferencia
			System.out.println("CLIENTE:Enviando Arquivo...");
			while ((bytesLidos = fileIn.read(buffer)) != -1) 
			{
				socketOut.write(buffer, 0, bytesLidos);
				socketOut.flush();
			}
			System.out.println("CLIENTE:Arquivo Enviado!");
			
			}
			//FIM DA OPÇÃO 2
			
		}
		catch(ConnectException e)
		{
			System.out.println("CLIENTE: ERRO: Não foi possivel conectar ao servidor");
		}
		
		catch (InputMismatchException e) 
		{
			System.out.println("CLIENTE: ERRO: Dingite um numero inteiro na opção");
		}
		
		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (conexao != null) 
			{
				try 
				{
					conexao.close();
				}
				catch (IOException e1) 
				{
					System.out.println("CLIENTE: ERRO de conexão");
				}
			}

			if (fileIn != null) 
			{
				try
				{
					fileIn.close();
				} 
				catch (IOException e1)
				{
					System.out.println("CLIENTE: ERRO de conexão");
				}
			}
			
			if (fileOut != null) 
			{
				try
				{
					fileOut.close();
				} 
				catch (IOException e1)
				{
					System.out.println("CLIENTE: ERRO de conexão");
				}
			}

			if (socketIn != null)
			{
				try 
				{
					socketIn.close();
				} 
				catch (IOException e1)
				{
					System.out.println("CLIENTE: ERRO de conexão");
				}
			}
			
			if (socketOut != null)
			{
				try 
				{
					socketOut.close();
				} 
				catch (IOException e1)
				{
					System.out.println("CLIENTE: ERRO de conexão");
				}
			}
		}

	}
}
