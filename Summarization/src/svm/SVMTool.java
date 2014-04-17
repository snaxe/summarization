package svm;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ChatStructure.EmailThread;
import ChatStructure.Message;
import ChatStructure.SingleMail;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightModel;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.TrainingParameters;

public class SVMTool {

  public static int N = 217; // number of training docs

  public static int M = 6; // max. number of features per doc
  private static FileInputStream fstream;
  private static DataInputStream in;
  private static BufferedReader br;
  
  SVMLightInterface trainer;
  LabeledFeatureVector[] traindata;
  SVMLightModel model;
  
 // public static void main(String[] args) throws Exception {
  public void SVMModelCreate(String uri, int n2) throws Exception{
	  N = n2;
    Random rd = new java.util.Random(new Date().getTime());

    // The trainer interface with the native communication to the SVM-light 
    // shared libraries
    trainer = new SVMLightInterface();

    // The training data
    traindata = new LabeledFeatureVector[N];
    TrainingParameters tp;
    // Sort all feature vectors in ascedending order of feature dimensions
    // before training the model
    SVMLightInterface.SORT_INPUT_VECTORS = true;

   // String uri = "E:\\Dropbox\\Dump\\bc3\\output1.dat";
    try{
		fstream =new FileInputStream(new File(uri));
		in=new DataInputStream(fstream) ;
		br = new BufferedReader(new InputStreamReader(in));
		String input;
		int num = 0;
		while((input = br.readLine()) != null && num <N ){
			String[] temp = input.split(" ");
			int output = Integer.parseInt(temp[0]);
			//Map<Integer, Double> keyValueMap = new HashMap<Integer, Double>();
			int[] dims = new int[M];
		    double[] values = new double[M];
		    
			for(int i = 1;i<temp.length ; i++){
				String[] keyvalue = temp[i].split(":");
			//	keyValueMap.put(Integer.parseInt(keyvalue[0]), Double.parseDouble(keyvalue[1]));
				dims[i-1] = Integer.parseInt(keyvalue[0]);
				values[i-1] = Double.parseDouble(keyvalue[1]);
			}
			 // Store dimension/value pairs in new LabeledFeatureVector object
		      traindata[num] = new LabeledFeatureVector(output, dims, values);

		      // Use cosine similarities (LinearKernel with L2-normalized input vectors)
		      traindata[num].normalizeL2();
		      num++;
		}
		
		br.close();
	}
	catch(IOException e){
		System.out.println("EXCEPTION : "+e.getMessage());
	}
    
    System.out.println(" DONE.");

    // Initialize a new TrainingParamteres object with the default SVM-light
    // values
    tp = new TrainingParameters();

    // Switch on some debugging output
    tp.getLearningParameters().verbosity = 1;

    System.out.println("\nTRAINING SVM-light MODEL ..");
    model = trainer.trainModel(traindata, tp);
    model.writeModelToFile("jni_model.dat");
    System.out.println(" DONE.");
  }
   
  public void SVMVerify(List<EmailThread> convers) throws MalformedURLException, ParseException{ 

	    // Use this to store a model to a file or read a model from a URL.
	    model = SVMLightModel.readSVMLightModelFromURL(new java.io.File("jni_model.dat").toURL());

//	     Use the classifier on the randomly created feature vectors
	    System.out.println("\nVALIDATING SVM-light MODEL in Java..");
	    
//	    for (int i = 0; i < N; i++) {
//
//	      // Classify a test vector using the Java object
//	      // (in a real application, this should not be one of the training vectors)
//	      double d = model.classify(traindata[i]);
//	      if ((traindata[i].getLabel() < 0 && d < 0)
//	          || (traindata[i].getLabel() > 0 && d > 0)) {
//	        precision++;
//	      }
//	      if (i % 10 == 0) {
//	        System.out.print(i + ".");
//	      }
//	    }
	    int a,b,c,d,e;
	    //tp = fp = tn = fn = 0;
	    a  = b  = c  = d  = e = 0 ;
	    int num = 0;
	    for(EmailThread et : convers){
	    	for(SingleMail mail : et.getMails()){
	    		for(Message msg : mail.getMessage()){
	    			num++;
	    			int[] dims = {1,2,3,4,5,6};
	    		    double[] values = new double[M];
	    		    values[0] = msg.getTFIDF();
	    		    values[1] = msg.getTFISF();
	    		    values[2] = msg.getSentiScore();
	    		    values[3] = msg.getLength();	    
	    		    values[4] = (msg.getQuestion() == true) ? 1 : 0;
	    		    values[5] = msg.getSimilarityscore();
	    		    int label = (msg.getSummery() == true) ? 1 : -1;
	    			LabeledFeatureVector testdata = new LabeledFeatureVector(label, dims, values);
	    			testdata.normalizeL2();
	    			double val = model.classify(testdata);
	    			
	    		    if ((testdata.getLabel() > 0 && val > 0)) {
	    		        a++;
	    		    }
	    		    else if(testdata.getLabel() > 0 && val < 0) 
	    		    	b++;
	    		    else if(testdata.getLabel() < 0 && val < 0)
	    		    	d++;
	    		    else if(testdata.getLabel() < 0 && val > 0) 
	    		    	c++;
	    		}
	    	}
	    }
	    System.out.println(a + " : " + b +" : " + c +" : " + d + " : " + num);
	    System.out.println(" DONE.");
	    double precision = ((double) (a) / (a+b));
	    System.out.println("\n" + precision
	        + " PRECISION ON RANDOM TRAINING SET.");
	    double recall = ((double) (a) / (a+c));
	    System.out.println("\n" + recall
		        + " RECALL ON RANDOM TRAINING SET.");
	    
	    System.out.println(2*(precision*recall)/(precision+recall) + " F1 Score");
	    System.out.println(5*(precision*recall)/(4*precision+recall) + " F2 Score");
	    
//	    System.out.println("\nVALIDATING SVM-light MODEL in Native Mode..");
//	    precision = 0;
//	    for (int i = 0; i < N; i++) {
//
//	      // Classify a test vector using the Native Interface
//	      // (in a real application, this should not be one of the training vectors)
//	      double d = trainer.classifyNative(traindata[i]);
//	      if ((traindata[i].getLabel() < 0 && d < 0)
//	          || (traindata[i].getLabel() > 0 && d > 0)) {
//	        precision++;
//	      }
//	      if (i % 10 == 0) {
//	        System.out.print(i + ".");
//	      }
//	    }
	   


	    System.out.println(" DONE.");
	    }
 }
