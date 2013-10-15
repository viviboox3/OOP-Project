package xtc.oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;
// FactoryFactory inherits runtime() from xtc.util.Tool;
import xtc.lang.FactoryFactory;


import xtc.lang.CPrinter;
import xtc.lang.JavaFiveParser;
import xtc.lang.JavaPrinter;
import xtc.util.SymbolTable;

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 */
public class Translator extends Tool {
    
    private SymbolTable table;

  /** Create a new translator. */
  public Translator() {
      table = new SymbolTable();
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
      bool("countMethods", "countMethods", false, "Count all Java methods.").
      bool("changeASTNodes", "changeASTNodes", false, "Changed the AST Nodes format").
      bool("chosenASTNode", "chosenASTNode", false, "Choose an AST Node");
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
    JavaFiveParser parser =
      new JavaFiveParser(in, file.toString(), (int)file.length());
    Result result = parser.pCompilationUnit(0);
    return (Node)parser.value(result);
  }
    
    public void createSymbolTable (Node node) {
        if (node.size() == 0) {
            table.enter(node);
            runtime.console().format(node).pln().flush();
        }
        else if (node.size() != 0) {
            int size = node.size();
            Object childNode = null;
            for (int x = 0; x < size; x++) {
                childNode = node.getNode(x);
                if (childNode != null) {
                    runtime.console().format((Node)childNode).pln().flush();
                    createSymbolTable((Node)childNode);
                }
            }
            table.enter(node);
        }
    }

  public void process(Node node) {
    if (runtime.test("printJavaAST")) {
      runtime.console().format(node).pln().flush();
    }

    if (runtime.test("printJavaCode")) {
      new JavaPrinter(runtime.console()).dispatch(node);
      runtime.console().flush();
    }
      
    if (runtime.test("changeASTNodes")) {
        GNode rootUnit = (GNode)node;
        // node = rootUnit;
        createSymbolTable(node);
        //runtime.console().format(symbolTable).pln().flush();
        //new CPrinter(runtime.console()).dispatch(rootUnit);
       // runtime.console().format(rootUnit).pln().flush();
    }
    
      if (runtime.test("chosenASTNode")) {
          Node childNode = node.getNode(1);
          runtime.console().format(childNode).pln().flush();
      }

    if (runtime.test("countMethods")) {
      new Visitor() {
        private int count = 0;

        public void visitCompilationUnit(GNode n) {
          visit(n);
          runtime.console().p("Number of methods: ").p(count).pln().flush();
        }

        public void visitMethodDeclaration(GNode n) {
          runtime.console().p("Name of node: ").p(n.getName()).pln();
          runtime.console().p("Name of method: ").p(n.getString(3)).pln();
          visit(n);
          count++;
        }

        public void visit(Node n) {
          for (Object o : n) if (o instanceof Node) dispatch((Node) o);
        
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
