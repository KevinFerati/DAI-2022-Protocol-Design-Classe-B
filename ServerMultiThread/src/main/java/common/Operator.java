package common;
public enum Operator {
    ADD("ADD"), SUB("SUB"), DIV("DIV"), MUL("MUL"), STP("STP");

    Operator(String op) {
        this.op = op;
    }
    private String op;
    public String getOp() {
        return op;
    }
    public String toString() {
        return getOp();
    }
}