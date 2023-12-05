package viewmodels

import api.ApiClient
import com.google.api.client.auth.oauth2.Credential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlinx.datetime.Clock
import org.models.opus.models.*
import ui.components.storeTheme


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

    private var _currentScreen = MutableStateFlow(savedStateHolder.consumeRestored("currentScreen") as String? ?: "/login")
    val currentScreen = _currentScreen.asStateFlow()

    private var _credential = MutableStateFlow(savedStateHolder.consumeRestored("credential") as Credential?)
    val credential = _credential.asStateFlow()

    private var _user = MutableStateFlow(savedStateHolder.consumeRestored("user") as User?)
    val user = _user.asStateFlow()

    private var _loading = MutableStateFlow(savedStateHolder.consumeRestored("Loading") as Boolean?)
    val loading = _loading.asStateFlow()

    private var _userName = MutableStateFlow(savedStateHolder.consumeRestored("username") as String?)
    val userName = _userName.asStateFlow()

    private var _pictureURL = MutableStateFlow(savedStateHolder.consumeRestored("picture") as String?)
    val pictureURL = _pictureURL.asStateFlow()

    private var _email = MutableStateFlow(savedStateHolder.consumeRestored("email") as String?)
    val email = _email.asStateFlow()

    private var _darkTheme = MutableStateFlow(savedStateHolder.consumeRestored("theme") as Boolean?)
    val darkTheme = _darkTheme.asStateFlow()

    private var _searchString = MutableStateFlow(savedStateHolder.consumeRestored("search") as String?)
    val searchString = _searchString.asStateFlow()

    private var _authLink = MutableStateFlow(savedStateHolder.consumeRestored("authLink") as String?)
    val authLink = _authLink.asStateFlow()

    fun setAuthLink(value: String?){
        _authLink.value = value
    }

    fun setSearchString(value: String){
        _searchString.value = value
    }

    fun setLoading(value: Boolean) {
        _loading.value = value
    }

    fun setDarkTheme(value: Boolean){
        _darkTheme.value = value
        storeTheme(value)
    }

    fun setEmail(value: String){
        _email.value = value
    }

    fun setUserName(value: String){
        _userName.value = value
    }

    fun setPictureURL(value: String){
        _pictureURL.value = value
    }

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

    fun setCurrentScreen(value: String) {
        _currentScreen.value = value
    }

    fun setCredential(value: Credential) {
        _credential.value = value
    }

    fun setUser(value: User) {
        _user.value = value
    }

    fun updateNote(
        note: Note,
        title: String? = null,
        body: String? = null,
        tags: List<Tag>? = null,
        pinned: Boolean? = null
    ){
        val updatedNote = Note(
            title ?: note.title,
            body ?: note.body,
            tags ?: note.tags,
            pinned ?: false,
            note.id
        )
        viewModelScope.launch {
            setNotes(ApiClient.getInstance().editNote(note.id, updatedNote))
        }
    }

    fun createNote(value: Note){
        viewModelScope.launch {
            setNotes(ApiClient.getInstance().postNote(value))
        }
    }

    fun deleteNote(value: Note){
        viewModelScope.launch {
            setNotes(ApiClient.getInstance().deleteNote(value.id))
        }
    }

    fun setTags(value: List<Tag>) {
        _tags.value = value
    }

    fun createTag(value: Tag){
        viewModelScope.launch {
            setTags(ApiClient.getInstance().postTag(value))
        }
    }

    fun updateTag(
        title: String? = null,
        colour: Colour? = null,
        tag: Tag
    ){
        val newTag = Tag(
            title = title ?: tag.title,
            colour = colour ?: tag.colour,
            id = tag.id
        )
        viewModelScope.launch {
            setTags(ApiClient.getInstance().editTag(tag.id, newTag))
        }
    }

    fun deleteTag(value: Tag){
        viewModelScope.launch {
            setTags(ApiClient.getInstance().deleteTag(value.id))
        }
    }

    fun updateTask(
        text: String? = null,
        completed: Boolean? = null,
        tagStatus: Map<Tag, Boolean>? = null,
        dueDate: LocalDateTime? = null,
        new: Boolean = false,
        task: Task? = null,
        important: Boolean? = null
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
                if (tagStatus != null) taskTags else task.tags,
                task.notificationSent,
                important ?: false,
            )
            viewModelScope.launch {
                setTasks(ApiClient.getInstance().editTask(task.id, taskToSend))
            }
        // If creating new task
        } else {
            val taskToSend = Task(
                completed ?: false,
                text ?: "",
                time.toLocalDateTime(TimeZone.currentSystemDefault()),
                dueDate,
                taskTags.toList(),
                false,
                important ?: false,
            )
            viewModelScope.launch {
                setTasks(ApiClient.getInstance().postTask(taskToSend))
            }
        }
    }

    fun deleteTask(
        task: Task?
    ) {
        if (task != null){
            viewModelScope.launch {
                setTasks(ApiClient.getInstance().deleteTask(task.id))
            }
        }
    }

    fun fetchAllData() {
        viewModelScope.launch {
            setTags(ApiClient.getInstance().getTags())
            setTasks(ApiClient.getInstance().getTasks())
            setNotes(ApiClient.getInstance().getNotes())
            setLoading(false)
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
        savedStateHolder.registerProvider("currentScreen"){
            currentScreen.value
        }
        savedStateHolder.registerProvider("credential"){
            credential.value
        }
        savedStateHolder.registerProvider("authLink"){
            authLink.value
        }
        setLoading(false)
    }
}