package analysis;



import PTAnalysis.PointsToAnalysis;
import sootup.core.graph.BasicBlock;
import sootup.core.graph.DominanceFinder;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.basic.LValue;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.*;
import sootup.java.core.interceptors.LocalLivenessAnalyser;
import sootup.java.core.types.JavaClassType;
import sootup.java.core.views.JavaView;

import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;

import java.util.*;
import java.util.stream.Stream;

public class my_analysis {  //unsure of what this class will do yet
    private  AnalysisInputLocation inputLocation;
    private  JavaView view;
    private  JavaClassType classType;
    private  JavaSootClass sootClass;
    private  MethodSignature methodSignature;
    private  Optional<JavaSootMethod> opt;
    private  SootMethod method;
    private LocalLivenessAnalyser live_analysis=null;
    private DominanceFinder dominator_analysis=null;
    //instantiates the above fields
    private  void init(){
        //location->view->classType->SootClass->method->SootMethod->cfg
        inputLocation =
                new JavaClassPathAnalysisInputLocation("subject_of_analysis/bytecode");
        view = new JavaView( inputLocation);
        classType =
                view.getIdentifierFactory().getClassType("A");
        sootClass = view.getClass(classType).get();
        
        methodSignature =
                view
                        .getIdentifierFactory()
                        .getMethodSignature(
                                classType,
                                "a", // method name
                                "A", // return type
                                List.of("A","int")); // args

        opt = view.getMethod(methodSignature);

        if(!opt.isPresent()){
            return;
        }
        method = opt.get();


    }

    String numbered_block_to_string(BasicBlock<?> block, Integer i){
        return (block.toString().substring(0,6)+ i+'\n' + block.toString().substring(6).
                replace(',','\n'));
    }
    private void live_variables(boolean print){

        live_analysis = (live_analysis == null)?new LocalLivenessAnalyser(method.getBody().getStmtGraph()) : live_analysis;

        if(print){
            System.out.println("--\nLIVE VARIABLE ANALYSIS");
            method.getBody().getStmts().stream().forEach(
                    x -> System.out.println(x +" "+ live_analysis.getLiveLocalsAfterStmt(x)));
        }

    }

    private void dominator_analysis(boolean print){

        dominator_analysis = (dominator_analysis == null)?new DominanceFinder(method.getBody().getStmtGraph()) : dominator_analysis;


        if(print){
            System.out.println("--\nDOMINATOR ANALYSIS:");
            Map<BasicBlock<?>, Integer>  blocks= dominator_analysis.getBlockToIdx();
            Stream<Map.Entry<BasicBlock<?>,Integer>> sorted_blocks =        //idk if the sorting means much
                        blocks.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue());


             sorted_blocks.forEach( block->  System.out.println(
                     numbered_block_to_string(block.getKey(), block.getValue()) +
                             " is immediately dominated by " +
                             blocks.get( dominator_analysis.getImmediateDominator(block.getKey()) ) )  );


                }
            }




    public static void main(String[] args) {
        my_analysis analysis= new my_analysis();
        analysis.init();

        System.out.println(analysis.method.getModifiers());

        /*
        CallGraphAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);
        CallGraph cg = cha.initialize(Collections.singletonList(methodSignature));


        System.out.println("--");
        cg.callsFrom(methodSignature).stream()
                .forEach(tgt -> System.out.println(methodSignature + " may call " + tgt));


        System.out.println("--");
        System.out.println("sootclass of " +analysis.sootClass);
        System.out.println("sootmethod of " +analysis.method.getBody());
        System.out.println("--");
        */
       // PrintClassesOfTypesOfDefinitions(analysis);

        PointsToAnalysis PTA = new PointsToAnalysis(analysis.view);
        PTA.analise(analysis.method);

        // analysis.live_variables(true);
        //analysis.dominator_analysis(true);
       // PrintMethodStmtLines(analysis);
    }

//debugging functions
    static void PrintMethodStmtLines(my_analysis analysis) {
        System.out.println("-----------");
        for (Stmt stmt : analysis.method.getBody().getStmts()) {
            System.out.println(stmt.getPositionInfo().getStmtPosition().getFirstLine());
            //System.out.println(stmt.getClass());
        }

    }

    static void PrintClassesOfTypesOfDefinitions(my_analysis analysis){
        for(LValue def : analysis.method.getBody().getDefs())
            System.out.println(def +"   "+ def.getType().getClass());
    }
}


