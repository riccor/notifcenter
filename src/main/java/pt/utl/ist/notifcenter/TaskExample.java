package pt.utl.ist.notifcenter;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

public class TaskExample extends CustomTask {

    @Override
    public void runTask() throws Exception {
        Group.dynamic("notificationAdmins").toPersistentGroup();
    }
}
