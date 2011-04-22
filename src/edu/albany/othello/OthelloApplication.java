package edu.albany.othello;

public class OthelloApplication {
    public static OthelloModel model;
    public static OthelloView view;
    
    static {
        model = new OthelloModel();
        view = new OthelloView();
        
        model.addUpdateListener(view);
        model.initialize();
    }
    
    public static void main(String[] args) {
        // TODO: Auto-generated method stub
    }
}
