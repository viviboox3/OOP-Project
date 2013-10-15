package xtc.oop;

// These 3 methods are needed for this file to function as a Java file inputting external files.
import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.parser.ParserBase;
import xtc.lang.CReader;
//import xtc.lang.JavaFiveParser; CParser Added.
import xtc.lang.CParser;

import xtc.lang.JavaPrinter;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 */
public class CTranslator extends Tool {

  /** Create a new translator. */
  public CTranslator() {
    // Nothing to do.
  }

  public String getName() {
    return "Java to C++ Translator";
  }

  public String getCopy() {
    return "(C) 2013 <Group Name>";
  }

  public void init() {
    super.init();

    // Declare command line arguments.
    runtime.
      bool("printJavaAST", "printJavaAST", false, "Print Java AST.").
      bool("printJavaCode", "printJavaCode", false, "Print Java code.").
      bool("countMethods", "countMethods", false, "Count all Java methods.");
  }

  public void prepare() {
    super.prepare();

    // Perform consistency checks on command line arguments.
  }

  public File locate(String name) throws IOException {
    File file = super.locate(name);
    if (Integer.MAX_VALUE < file.length()) {
      throw new IllegalArgumentException(file + ": file too large");
    }
    return file;
  }

  public Node parse(Reader in, File file) throws IOException, ParseException {
      // JaveFiveParser changed to CParser
    CParser parser =
      new CParser(in, file.toString(), (int)file.length());
      //parser.pCompilationUnit changed to parser.pTranslationUnit
    Result result = parser.pTranslationUnit(0);
      //char *boolut;
    return (Node)parser.value(result);
  }

  public void process(Node node) {
    if (runtime.test("printJavaAST")) {

        boolean yesTransversal = node.hasTraversal();
        if (yesTransversal) {
           runtime.console().p("Yes traversal exists");
        }
        runtime.console().p("There is something here that isn't right...");
    }

    if (runtime.test("printCCode")) {
     // new CPrinter(runtime.console()).dispatch(node);
      //runtime.console().flush();
        
    }

    if (runtime.test("countMethods")) {
      new Visitor() {
        private int count = 0;

        public void visitCompilationUnit(GNode n) {
          visit(n);
            boolean yesTransversal = n.hasTraversal();
            if (yesTransversal) {
                runtime.console().p("Yes traversal exists");
            }
            else {
                runtime.console().p("No it doesn't have it");
            }
          runtime.console().p("LOLOLOLLOL: ").p(count).pln().flush();
        }

        public void visitMethodDeclaration(GNode n) {
            boolean yesTransversal = n.hasTraversal();
            if (yesTransversal) {
                runtime.console().p("Yes traversal exists");
            }
            else {
                runtime.console().p("No it doesn't have it");
            }
          runtime.console().p("Name of node: ").p(n.getName()).pln();
          runtime.console().p("Name of method: ").p(n.getString(3)).pln();
          visit(n);
          count++;
        }

        public void visit(Node n) {
          for (Object o : n) if (o instanceof Node) dispatch((Node) o);
            runtime.console().p("This is the visit method haahaha ").pln().flush();
        }

      }.dispatch(node);
    }
  }

  /**
   * Run the translator with the specified command line arguments.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    //Translator t = new Translator();
    //t.run(args);

    new Translator().run(args);
  }

}
