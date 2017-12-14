import javafx.scene.Node;

public interface CopyAble {
    Expression trueCopy();
    String convertToStringFlat();
    void setNode(Node n);
}
