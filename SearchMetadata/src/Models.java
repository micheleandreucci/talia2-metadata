import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Models implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<Model> matrixModels = new ArrayList<Model>();

	public void SaveModels() throws IOException {
		FileOutputStream outFile = new FileOutputStream("saved.txt");
		@SuppressWarnings("resource")
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);
		outStream.writeObject(this.matrixModels);
		outStream.close();

	}

	@SuppressWarnings("unchecked")
	public void loadModels() throws ClassNotFoundException, IOException {
		File filepath = new File("saved.txt");
		if (filepath.exists()) {
			FileInputStream inFile = new FileInputStream(filepath);
			ObjectInputStream in = new ObjectInputStream(inFile);
			this.matrixModels = (ArrayList<Model>) in.readObject();
			in.close();
		}
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException {
		/*
		 * Model prova=new Model(); prova.setAuthor("lorenzo");
		 * prova.setCollection("collection"); Model prova1=new Model();
		 * prova1.setAuthor("fernando"); prova1.setCollection("collection1"); Models
		 * m=new Models(); m.matrixModels.add(prova); m.matrixModels.add(prova1);
		 */
		Models m = new Models();
		m.loadModels();
		System.out.println(m.matrixModels.get(1).getAuthor());

	}
}
