package viewmodels

import api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.models.opus.models.Note
import org.models.opus.models.Tag
import org.models.opus.models.Task
import kotlinx.datetime.Clock


class MainViewModel(
    savedStateHolder: SavedStateHolder
) : ViewModel() {

    private var _tasks = MutableStateFlow(savedStateHolder.consumeRestored("tasks") as List<Task>? ?: listOf())
    val tasks = _tasks.asStateFlow()

    private var _notes = MutableStateFlow(savedStateHolder.consumeRestored("notes") as List<Note>? ?: listOf())
    val notes = _notes.asStateFlow()

    private var _tags = MutableStateFlow(savedStateHolder.consumeRestored("tags") as List<Tag>? ?: listOf())
    val tags = _tags.asStateFlow()

    private var _currentTag = MutableStateFlow(savedStateHolder.consumeRestored("currentTag") as Tag?)
    val currentTag = _currentTag.asStateFlow()

    private var _curDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    val curDate = _curDate.asStateFlow()

    fun setCurDate(value: LocalDateTime){
        _curDate.value = value
    }

    fun setCurrentTag (value: Tag?){
        _currentTag.value = value
    }

    fun setTasks(value: List<Task>) {
        _tasks.value = value
    }

    fun setNotes(value: List<Note>) {
        _notes.value = value
    }

    fun updateNote(
        note: Note,
        title: String? = null,
        body: String? = null,
        tags: List<Tag>? = null
    ){
        val updatedNote = Note(
            title ?: note.title,
            body ?: note.body,
            tags ?: note.tags,
            note.id
        )
        viewModelScope.launch {
            setNotes(ApiClient.getInstance().editNote(0, note.id, updatedNote))
        }
    }

    fun createNote(value: Note){
        viewModelScope.launch {
            setNotes(ApiClient.getInstance().postNote(0, value))
        }
    }

    fun deleteNote(value: Note){
        viewModelScope.launch {
            setNotes(ApiClient.getInstance().deleteNote(0, value.id))
        }
    }

    fun setTags(value: List<Tag>) {
        _tags.value = value
    }

    fun createTag(value: Tag){
        viewModelScope.launch {
            setTags(ApiClient.getInstance().postTag(0, value))
        }
    }

    fun deleteTag(value: Tag){
        viewModelScope.launch {
            setTags(ApiClient.getInstance().deleteTag(0, value.id))
        }
    }

    fun updateTask(
        text: String? = null,
        completed: Boolean? = null,
        tagStatus: Map<Tag, Boolean>? = null,
        dueDate: LocalDateTime? = null,
        new: Boolean = false,
        task: Task? = null
    ) {
        // Convert tag status map to list of tags
        val taskTags = mutableListOf<Tag>()
        tagStatus?.forEach { entry ->
            if (entry.value) {
                taskTags.add(entry.key)
            }
        }
        // Grab timestamp
        val time = Clock.System.now()

        // If updating task
        if (!new && task != null) {
            val taskToSend = Task(
                completed ?: task.completed,
                text ?: task.action,
                time.toLocalDateTime(TimeZone.currentSystemDefault()),
                dueDate?: task.dueDate,
                if (tagStatus != null) taskTags else task.tags
            )
            viewModelScope.launch {
                setTasks(ApiClient.getInstance().editTask(0, task.id, taskToSend))
            }
        // If creating new task
        } else {
            val taskToSend = Task(
                completed ?: false,
                text ?: "",
                time.toLocalDateTime(TimeZone.currentSystemDefault()),
                dueDate,
                taskTags.toList()
            )
            viewModelScope.launch {
                setTasks(ApiClient.getInstance().postTask(0, taskToSend))
            }
        }
    }

    fun deleteTask(
        task: Task?
    ) {
        if (task != null){
            viewModelScope.launch {
                setTasks(ApiClient.getInstance().deleteTask(0, task.id))
            }
        }
    }

    fun fetchAllData() {
        viewModelScope.launch {
            setTasks(ApiClient.getInstance().getTasks(0))
            setNotes(ApiClient.getInstance().getNotes(0))
            setTags(ApiClient.getInstance().getTags(0))
        }
    }

    init {
        savedStateHolder.registerProvider("tasks") {
            tasks.value
        }
        savedStateHolder.registerProvider("notes") {
            notes.value
        }
        savedStateHolder.registerProvider("tags") {
            tags.value
        }
    }
}