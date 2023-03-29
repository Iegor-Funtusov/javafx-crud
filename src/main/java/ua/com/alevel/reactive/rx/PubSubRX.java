package ua.com.alevel.reactive.rx;

import io.reactivex.Observer;
import io.reactivex.subjects.BehaviorSubject;
import ua.com.alevel.config.LoaderPage;
import ua.com.alevel.persistence.entity.Department;

public class PubSubRX {

    private static final PubSubRX instance = new PubSubRX();
    private final BehaviorSubject<LoaderPage> behaviorSubjectLoaderPage =
            BehaviorSubject.createDefault(LoaderPage.DEPARTMENT);
    private final BehaviorSubject<Department> behaviorSubjectDepartment =
            BehaviorSubject.create();

    private PubSubRX() { }

    public static PubSubRX getInstance() {
        return instance;
    }

    public void publishLoaderPage(LoaderPage page) {
        behaviorSubjectLoaderPage.onNext(page);
    }

    public void subscribeLoaderPage(Observer<LoaderPage> observer) {
        behaviorSubjectLoaderPage.subscribe(observer);
    }

    public void publishDepartment(Department department) {
        behaviorSubjectDepartment.onNext(department);
    }

    public void subscribeDepartment(Observer<Department> observer) {
        behaviorSubjectDepartment.subscribe(observer);
    }
}
