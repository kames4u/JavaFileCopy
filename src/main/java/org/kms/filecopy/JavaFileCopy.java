package org.kms.filecopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

public class JavaFileCopy {

	public static void main(String[] args) throws InterruptedException, IOException, ParseException {

		// create Options object
		Options options = new Options();

		// add option "-f"
		options.addOption("f", true, "from address");
		// add option "-t"
		options.addOption("t", true, "to address");
		// add option "-m"
		options.addOption("m", true, "message file name");
		// add option "-c"
		options.addOption("c", true, "copy count");
		// add option "-s"
		options.addOption("s", true, "source file name with absolute path");
		// add option "-s"
		options.addOption("d", true, "destination path (absolute path of destination)");				
		// add option "-h"
		options.addOption("h", false, "help");

		// ***Parsing Stage***
		// Create a parser
		CommandLineParser parser = new DefaultParser();

		// parse the options passed as command line arguments
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("h")) {
			help(options);
		}
		
		if (!(cmd.hasOption("f") && cmd.hasOption("t") && cmd.hasOption("m") 
				&& cmd.hasOption("c") && cmd.hasOption("d") && cmd.hasOption("s"))) {
			help(options);
		}
		
		String fromAddr = cmd.getOptionValue("f");
		String toAddr = cmd.getOptionValue("t");
		String msgName = cmd.getOptionValue("m");
		String sourcePath = cmd.getOptionValue("s");
		String destPath = cmd.getOptionValue("d");
		int noOfFiles = Integer.parseInt(cmd.getOptionValue("c"));
		
		// ** Test **
		// String fromAddr = "ram@direct.loadtest1.dev.masshiwaystage.com";
		// String toAddr = "syndromic@direct.syndromic.dev.masshiwaystage";
		// String filename = "testmessage";
		// int noOfFiles = Integer.parseInt("1000");

		if(!(destPath.endsWith("/") || destPath.endsWith("\\"))) {
			destPath = destPath + "/";
		}
		
		File source = new File(sourcePath);
		String finalName = "";
		long start = System.nanoTime();

		for (int count = 1; count <= noOfFiles; count++) {

			finalName = fromAddr + ".." + toAddr + "##" + count + "#" + msgName + ".txt";
			File dest = new File(destPath + finalName);

			// copy file conventional way using Stream
			copyFileUsingStream(source, dest);
		}

		System.out.println("Source Copied -- " + sourcePath);
		System.out.println("Destnation of copied files -- " + destPath);
		System.out.println("Time taken to Copy -- " + noOfFiles + " files == " + (System.nanoTime() - start) + " nano seconds");
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	private static void help(Options options) {
		// This prints out some help
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("Copy File", options);

		System.exit(0);
	}

}
