package common;
public enum Results {
    ER("ER"), OK("OK");

    Results(String res) {
        this.result = res;
    }
    private String result;
    public String getResult() {
        return result;
    }
    public String toString() {
        return getResult();
    }
}