import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager { // все методы и поля статические, потому что разных объектов-менеджеров не будет

    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static HashMap<Integer, Epic> epics = new HashMap<>();

    private static int id = 1; // поле общее для всех, при создании объекта присваивается полю id в тасках, при создании ++

    public static ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer i : tasks.keySet()) {
            tasksList.add(tasks.get(i));
        }
        return tasksList;
    }

    public static ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            subTasksList.add(subTasks.get(i));
        }
        return subTasksList;
    }

    public static ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            epicsList.add(epics.get(i));
        }
        return epicsList;
    }

    public static Task getTaskById(int id) {
        return tasks.get(id);
    }

    public static SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public static Epic getEpicById(int id) {
        return epics.get(id);
    }

    // Методы создания тасков возвращают id созданной таски для возможности дальнейшего обращения к ней
    public static int createTask(Task task) {
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        return id++;
    }

    // сабтаск не существует отдельно от своего эпика, поэтому при ее создании вместе с объектом сабтаски передается id эпика
    public static int createSubTask(SubTask subTask, Integer epicId) {
        subTask.setId(id);
        subTask.setEpicId(epicId);
        subTask.setStatus(Status.NEW);
        subTasks.put(id, subTask);
        epics.get(epicId).getSubTasks().put(id, subTask);
        return id++;
    }

    public static int createEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
        return id++;
    }

    public static void updateTask(Task task) {
        Task oldTask = tasks.get(task.getId());
        oldTask.setStatus(task.getStatus());
        oldTask.setName(task.getName());
        oldTask.setDescription(task.getDescription());
    }

    public static void updateSubTask(SubTask subTask) {
        SubTask oldSubTask = subTasks.get(subTask.getId());
        oldSubTask.setName(subTask.getName());
        oldSubTask.setDescription(subTask.getDescription());
        oldSubTask.setStatus(subTask.getStatus());

        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasks().put(subTask.getId(), oldSubTask);
        epic.updateStatus(); // обновление статуса эпика, к которому принадлежит subTask
    }

    public static void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

    public static void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public static void removeSubTask(int subTaskId) {
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).getSubTasks().remove(subTaskId); //Удаление сабтаски из мапы эпика
        subTasks.remove(subTaskId); //Удаление сабтаски из общей мапы

        epics.get(epicId).updateStatus();
    }

    public static void removeEpic(int epicId) {
        for (Integer i : epics.get(epicId).getSubTasks().keySet()) {
            subTasks.remove(i);
        }
        epics.remove(epicId);
    }

    public static ArrayList<SubTask> getSubTasksByEpic(int epicId) {

        return epics.get(epicId).getSubTasksList();
    }

}
