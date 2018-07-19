import java.io.File; 

public class listarDiretorio { 

	public File[] ver(String diretorio)
	{ 
		File file = new File(diretorio); 
		File afile[] = file.listFiles(); 
		return afile;
	} 

} 