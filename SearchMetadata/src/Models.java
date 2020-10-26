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
	public static ArrayList<Model> matrixModels = new ArrayList<Model>();

	public void SaveModels() throws IOException {
		FileOutputStream outFile = new FileOutputStream("saved.txt");
		@SuppressWarnings("resource")
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);
		outStream.writeObject(Models.matrixModels);
		outStream.close();

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Model> loadModels() throws ClassNotFoundException, IOException {
		File filepath = new File("saved.txt");
		if (filepath.exists()) {
			FileInputStream inFile = new FileInputStream(filepath);
			ObjectInputStream in = new ObjectInputStream(inFile);
			matrixModels = (ArrayList<Model>) in.readObject();
			in.close();
		}
		return matrixModels;
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException {

		Model prova = new Model();
		prova.setAuthor("lorenzo");
		prova.setCollection("collection");
		Model prova1 = new Model();
		prova1.setAuthor("fernando");
		prova1.setCollection("collection1");
		Models m = new Models();
		Models.matrixModels.add(prova);
		Models.matrixModels.add(prova1);
		m.SaveModels();
		for (int i = 0; i < matrixModels.size(); i++) {
			System.out.println(""+Models.loadModels().get(i).getAuthor());
		}
		System.out.println(Models.matrixModels.get(1).getAuthor());
		System.out.println(Models.matrixModels.get(1).getCollection());
//		System.out.println(Models.matrixModels.toString());
		/*
		 * Models m = new Models();
		 * 
		 * m.loadModels(); System.out.println(m.matrixModels.get(1).getAuthor());
		 */

	}
}
