package at.srfg.robxtask.registration.openapi.task;

import at.srfg.robxtask.registration.exception.TaskAlreadyExistsException;
import at.srfg.robxtask.registration.exception.TaskNotAcceptableException;
import at.srfg.robxtask.registration.openapi.api.TaskApi;
import at.srfg.robxtask.registration.openapi.model.Task;
import at.srfg.robxtask.registration.persistence.MongoConnector;
import at.srfg.robxtask.registration.security.AsyncUserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoCollection;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Controller
@Api(tags = {"task"})
public class TaskController implements TaskApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TaskController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Autowired
    private MongoConnector mongo;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Autowired
    private AsyncUserInfoService userInfoService;

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Override
    public ResponseEntity<Void> addTask(Task body) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    MongoCollection<Document> tasks = getTaskCollection();
                    final String taskID = body.getTaskID();
                    Document task = tasks.find(new Document("TaskID", taskID)).first();
                    if (task != null) {
                        throw new TaskAlreadyExistsException(taskID);
                    }
                    try {
                        final String ublPersonID = userInfoService.resolve().getUblPersonID();
                        body.setTaskOwner(ublPersonID);
                        log.debug("Inserting new task with TaskID {} for TaskOwner {}", taskID, ublPersonID);
                        tasks.insertOne(Document.parse(mapperBuilder.build().writeValueAsString(body)));
                        break; // insert only once, even if more content-types are in request
                    } catch (JsonProcessingException e) {
                        throw new TaskNotAcceptableException(e.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private MongoCollection<Document> getTaskCollection() {
        return mongo.getCollection("tasks");
    }

}
