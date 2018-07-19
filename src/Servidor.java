import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;



public class Servidor 
{
	public static void main(String[] args) throws ConnectException 
	{ 
		       // Criando servidor
				Servidor server = new Servidor();

				// Aguardar conexao de cliente para transferia
				server.transfereCliente();
	}
	
	
	public void transfereCliente() throws ConnectException
	{
		//Diretorio onde está os arquivos no servidor
		String diretorio = "C:\\Users\\Thiago\\Desktop\\Servidor\\"; 

		
		//Inicialização 
		InputStream socketIn = null;
		OutputStream socketOut = null;
		
		ServerSocket servidor = null;
		Socket conexao = null;
		
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		
		ObjectInputStream input =null; 
		ObjectOutputStream output = null;
		
		//Recebe nome do arquivo que cliente deseja receber
		String mensagem = "";
		
		//Menu que sera impresso no computador cliente
		String menu = "_________________MENU_____________________" +
				      "\n1 - Receber arquivo do Servidor"+
				       "\n2 - Enviar arquivo para o Servidor" +
				      "\n___________________________________________";	
		int opcao;//variavel guarda a opção digitada pelo computador cliente
		
		// Caso cliente escolha a opção 2 do menu será solicitado que ele digite uma senha para
		//verificar se o mesmo possui permissão para enviar o arquivo para o servidor
		String senha = "redes"; //Senha universal
		String senhaCliente=""; //Variavel responsável por guardar a senha digitada pelo cliente
		int respSenha1=1;	// Se senhaCliente e senha forem iguais retorna respSenha1 senão retorna respSenha2
		int respSenha2=0;
		

		try {
			
			// Abrindo porta para conexao de cliente
			servidor = new ServerSocket(12345);
			System.out.println("SERVIDOR: Foi realizada a abertura da porta 12345");

			// Aceita conexão de cliente
			conexao = servidor.accept();
			System.out.println("SERVIDOR: Conexão estabelecida");
			socketIn=conexao.getInputStream();
			
			//Configuracao do objeto de saida 
			output = new ObjectOutputStream(conexao.getOutputStream());
			//Configuração de objetos de entrada
			input = new ObjectInputStream(conexao.getInputStream());
			
			//Envia o menu para o cliente
			output.writeObject(menu);
			opcao=(int) input.readObject(); //variável opcao recebe a opção digitada pelo computador cliente
			
		
			if(opcao==1)  //(1 - Receber arquivo do Servidor)
			{			
			//Rotina para listar conteudo do diretorio no cliente
			listarDiretorio v = new  listarDiretorio();
			File[] afile;
			afile=v.ver(diretorio);
			output.writeObject(afile);//envia a lista de arquivos no diretorio para o cliente
			
			//Recebe do cliente o nome do arquivo que deve ser enviado pelo servidor
			mensagem = (String) input.readObject();
			
			
			// Criando buffer para realizar a leitura
			byte[] buffer = new byte[1024];
			int bytesLidos;
						  
			// Arquivo que sera transferido pelo servidor
			File f1 = new File(diretorio+mensagem);
			fileIn = new FileInputStream(f1);
			System.out.println("SERVIDOR:Lendo arquivo...");
			
			// Criando canal de transferencia
			socketOut = conexao.getOutputStream();

			// Lendo arquivo  e enviado para o canal de transferencia
			System.out.println("SERVIDOR:Enviando Arquivo...");
			while ((bytesLidos = fileIn.read(buffer)) != -1) 
			{
				socketOut.write(buffer, 0, bytesLidos);
				socketOut.flush();
			}
			System.out.println("SERVIDOR:Arquivo Enviado!");
			
			}
			//FIM OPÇÃO 1
			
			if(opcao==2) //(2 - Enviar arquivo para o Servidor)
			{
				
				//receber arquivo
				senhaCliente = (String) input.readObject(); //senhaCliente recebe a senha digitada pelo computador cliente
				
				//Tratamento de erro de senha
				while(!senha.equals(senhaCliente))
				{
					output.writeObject(respSenha2);
					output.flush();
					senhaCliente = (String) input.readObject();
				} 
				//Fim tratamento de erro de senha

					output.writeObject(respSenha1);
					//Recebe do cliente o nome do arquivo que o servidor ira receber
					mensagem = (String) input.readObject();
					// Arquivo recebido pelo cliente
					fileOut = new FileOutputStream(new File(diretorio+mensagem));
					System.out.println("SERVIDOR: Arquivo Salvo em:"+diretorio);
					
					// Prepara variaveis para transferencia
					byte[] buffer = new byte[1024];
					int bytesLidos;

					// Copia conteudo do canal
					System.out.println("SERVIDOR: Recebendo arquivo...");
					while ((bytesLidos = socketIn.read(buffer)) != -1) 
					{
						fileOut.write(buffer, 0, bytesLidos);
						fileOut.flush();
					}
					
					System.out.println("SERVIDOR: Arquivo recebido!");

			}
		
				
			
		} 

		catch (EOFException e)
		{
			System.out.println("SERVIDOR: ERRO: Problema na troca de dados");
		}
		catch(SocketException e)
		{
			System.out.println("SERVIDOR: ERRO: Problema na conexão");
		}
		catch (Exception e) 
		{
			// Mostra erro no console
			e.printStackTrace();
		}
		finally
		{
			if (socketIn != null)
			{
				try
				{
					socketIn.close();
				}
				catch (IOException e) 
				{
					System.out.println("SERVIDOR: ERRO: Não foi possivel realizar conexão");
				}
			}
			
			if (socketOut != null)
			{
				try
				{
					socketOut.close();
				}
				catch (IOException e) 
				{
					System.out.println("SERVIDOR: ERRO: Não foi possivel realizar conexão");
				}
			}

			if (servidor != null)
			{
				try 
				{
					servidor.close();
				} 
				catch (IOException e) 
				{
					System.out.println("SERVIDOR: ERRO: Não foi possivel realizar conexão");
				}
			}

			if (fileIn != null)
			{
				try
				{
					fileIn.close();
				}
				catch (IOException e) 
				{
					System.out.println("SERVIDOR: ERRO: Não foi possivel realizar conexão");
				}
			}
			
			if (fileOut != null)
			{
				try
				{
					fileOut.close();
				}
				catch (IOException e) 
				{
					System.out.println("SERVIDOR: ERRO: Não foi possivel realizar conexão");
				}
			}
		}
	}
 }

