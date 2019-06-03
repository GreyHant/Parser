package exam;

import java.util.Set;

public interface Presenter {
    void showResult(Set<String> fullFacts);
    void showError(String error);
}
