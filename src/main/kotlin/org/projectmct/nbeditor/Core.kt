import org.projectmct.nbeditor.core.AbstractModule
import org.projectmct.nbeditor.core.QueuedTask
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*

private const val SEC_NANO: Long = 1000000000

abstract class Core : CoroutineScope {
    // The number of updates in one second normally, -1 meaning infinity.
    var standardFrameSpeed: Int = -1

    // The maximum number of asynchronous tasks that can be executed simultaneously, please modify before initialize.
    var taskExecutes: Int = 8

    private var timeDelta: Long = 0

    private val syncModules: ArrayList<AbstractModule> = ArrayList()
    private val asyncModules: LinkedHashMap<AbstractModule, Job> = LinkedHashMap()

    private val taskMap: HashMap<QueuedTask, Job> = HashMap()
    private val taskQueue: LinkedList<QueuedTask> = LinkedList()

    // Last main loop refresh interval.
    fun timeDelta(): Long = timeDelta

    // The entry point required by the program, this method is called directly at startup.
    suspend fun launch() {
        coroutineScope {
            syncModules.forEach { it.initialize() }
            asyncModules.forEach { it.key.initialize() }
            init()

            val asyncJob = launch(Dispatchers.Default) {
                while (isActive && taskQueue.isNotEmpty() && asyncModules.size < taskExecutes) {
                    val task = taskQueue.removeFirst()
                    val job = launch { task.execute() }
                    taskMap[task] = job
                }
            }
            var lastTime = System.nanoTime()

            while (asyncJob.isActive) {
                timeDelta = System.nanoTime() - lastTime
                lastTime = System.nanoTime()

                syncModules.forEach { it.loop() }
                mainLoop()

                val waitTime =
                    System.nanoTime() + (SEC_NANO / standardFrameSpeed - System.nanoTime() + lastTime).coerceAtLeast(0)
                delay(waitTime / 1000000) // nanosecond to millisecond
            }
        }
    }


    /**Add synchronous or asynchronous work modules to the core
     *
     * @param module work module that added
     * @param async Whether the module is asynchronously refreshed
     * @param init Whether to initialize the module immediately, please avoid performing immediate initialization before startup,
     *             which will cause the module to be initialized repeatably
     */
    fun addModule(module: AbstractModule, async: Boolean = false, init: Boolean = false) {
        if (init) module.initialize()
        if (async) {
            val job = launch {
                while (coroutineContext.isActive) {
                    val last = System.nanoTime()
                    module.loop()
                    val wait = (SEC_NANO / module.standardFrameSpeed - System.nanoTime() + last).coerceAtLeast(0)
                    delay(wait / 1000000) // nanosecond to millisecond
                }
            }
            asyncModules[module] = job
        } else {
            syncModules.add(module)
        }
    }

    /**Remove a working module from the core
     *
     * @param module The work module that removed
     */
    fun removeModule(module: AbstractModule) {
        syncModules.remove(module)
        asyncModules.remove(module)?.cancel()
    }

    fun queueTask(task: Runnable): QueuedTask {
        val t = object : QueuedTask(task) {
            override fun finish() {
                taskMap[this]?.cancel()
                super.finish()
            }
        }
        taskQueue.add(t)
        return t
    }

    fun queuedTasks(): Int {
        return taskQueue.size
    }

    abstract fun init()
    abstract fun mainLoop()

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main
}