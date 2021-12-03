package at.srfg.robxtask.registration.openapi.task;

import at.srfg.robxtask.registration.openapi.api.ApiUtil;
import at.srfg.robxtask.registration.openapi.api.TasksApi;
import at.srfg.robxtask.registration.openapi.model.Task;
import at.srfg.robxtask.registration.persistence.MongoConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Api(tags = {"tasks"})
public class TasksController implements TasksApi {

    private final NativeWebRequest request;

    @Autowired
    public TasksController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    private static final Logger log = LoggerFactory.getLogger(TasksController.class);

    @Autowired
    private MongoConnector mongo;

    @Override
    public ResponseEntity<List<Task>> getTasks() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    try {
                        final ObjectMapper mapper = new ObjectMapper();
                        MongoCollection<Document> tasksCol = getTaskCollection();
                        final FindIterable<Document> tasks = tasksCol.find().projection(Projections.fields(Projections.exclude("_id")));
                        List<Task> res = new ArrayList<>();
                        for (Document task : tasks) {
                            res.add(mapper.readValue(task.toJson(), Task.class));
                        }
                        ApiUtil.setExampleResponse(request, "application/json", mapper.writeValueAsString(res));
                        break;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return new ResponseEntity<>(HttpStatus.OK);
}

    private MongoCollection<Document> getTaskCollection() {
        return mongo.getCollection("tasks");
    }

}
