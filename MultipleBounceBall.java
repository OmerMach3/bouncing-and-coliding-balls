
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author nurha
 */
public class MultipleBounceBall extends Application {
    static ArrayList<Double>Yaricaplar = new ArrayList<>();
    static ArrayList<Circle>Cemberler = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) {
        MultipleBallPane ballPane = new MultipleBallPane();
        ballPane.setStyle("-fx-set-border-color:yellow");
        ScrollBar bar = new ScrollBar();
        Button btad = new Button("+");
        Button btSubtract = new Button("-");
        Button suspend = new Button("Suspend");
        Button resume = new Button("Resume");
        HBox hb = new HBox(btad,btSubtract);
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);
        ballPane.setOnMouseClicked(e->{
            if(ballPane.getStatus())
            ballPane.pause();
            
            else 
            ballPane.play();
            
        });
        
        BorderPane pane = new BorderPane();
        pane.setTop(bar);
        pane.setCenter(ballPane);
        pane.setBottom(hb);
        btad.setOnAction(e->ballPane.add());
        btSubtract.setOnAction(e->ballPane.subtract());
        bar.setMax(20);
        bar.setValue(10);
        ballPane.rateProperty().bind(bar.valueProperty());
        
        Scene scene = new Scene(pane,300,300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Zıplayan Top");
        primaryStage.show();
    }

    class Ball extends Circle{
    private double dx = 1,dy=1;
    Ball(double x,double y,double radius,Color color){
        super(x,y,radius);
        this.setFill(color);
        
    }
    }
    private class MultipleBallPane extends Pane{
        private boolean isPlaying; 
        private Timeline animation;
        public MultipleBallPane(){
            animation = new Timeline(new KeyFrame(Duration.millis(50),e->moveBall()));
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play();
            
            
        }
        public boolean getStatus(){
                return isPlaying;
        }
        public void add(){
            Color color = new Color(Math.random(),Math.random(),Math.random(),0.5);
            getChildren().add(new Ball(new Random().nextInt(40)+20,new Random().nextInt(40)+20,new Random().nextInt(19)+2,color));
        }
        public void subtract(){
            if(getChildren().size()>0){
            Cemberler.clear();
            Yaricaplar.clear();
            for(int i = 0;i<getChildren().size();i++){
                if(getChildren().get(i) instanceof Circle){
                    Cemberler.add((Circle)getChildren().get(i));
                }
                
            }
            for(int i = 0;i<Cemberler.size();i++){
                Yaricaplar.add(Cemberler.get(i).getRadius());
                
            }
            double max = Collections.max(Yaricaplar);
            int index = Yaricaplar.indexOf(max);
            
            
                getChildren().remove(Cemberler.get(index));
            }
            
        }
       
        public void play(){
            animation.play();
            isPlaying = true;
        }
        public void pause(){
            animation.pause();
            isPlaying = false;
          }
        public void increaseSpeed(){
            
            animation.setRate(animation.getRate()+0.1);
            
        }
        public void decreaseSpeed(){
            animation.setRate(animation.getRate()>0 ? animation.getRate()-0.1:0);
        }
        public DoubleProperty rateProperty(){
            return animation.rateProperty();
            
        }
        protected void moveBall(){
            for(int i= 0;i<this.getChildren().size();i++){
                
                Ball ball = (Ball)this.getChildren().get(i);
                if(this.getChildren().size()>=2){
                for(int j = i+1;j<this.getChildren().size();j++){
                    
                    
                    Ball ball_interaction = (Ball)this.getChildren().get(j);
                    
                if(does_collide(ball,ball_interaction)>=0){
                    
                    ball.setRadius(ball_interaction.getRadius()+ball.getRadius());
                    this.getChildren().remove(this.getChildren().get(j));
                    
                }    
                }
                }
                if(ball.getCenterX()<ball.getRadius()||ball.getCenterX()>getWidth()-ball.getRadius()){
                    ball.dx*=-1;
                }
                
                
                if(ball.getCenterY()<ball.getRadius()||ball.getCenterY()>getHeight()-ball.getRadius()){
                    ball.dy*=-1;
                }
                ball.setCenterX(ball.dx+ball.getCenterX());
                ball.setCenterY(ball.dy+ball.getCenterY());
                
            }
            
            
            
        }
        
    }
    public static int does_collide(Ball b1,Ball b2){
        double distSq = (b1.getCenterX()-b2.getCenterX())*(b1.getCenterX()-b2.getCenterX())
                            *(b1.getCenterY()-b2.getCenterY())*(b1.getCenterY()-b2.getCenterY());
        
        double radSumSq = (b1.getRadius()+b2.getRadius())*(b1.getRadius()+b2.getRadius());
        
        if(distSq==radSumSq){
            return 1;
        }
        else if(distSq>radSumSq){
            return -1;
        }
        else 
            return 0;
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
