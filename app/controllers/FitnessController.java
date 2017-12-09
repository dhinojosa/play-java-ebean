package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import models.Exercise;
import play.cache.AsyncCacheApi;
import play.cache.Cached;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FitnessController extends Controller {

    private FormFactory formFactory;
    private AsyncCacheApi cache;

    @Inject
    public FitnessController(FormFactory formFactory, AsyncCacheApi cache) {
        this.formFactory = formFactory;
        this.cache = cache;
    }

    public Result welcome() {
        return ok("Welcome to Borg Fitness! Time to assimilate into fitness!");
    }

    public Result welcomeWithName(String name) {
        return ok(String.format("Welcome to Borg Fitness %s! Time to assimilate into fitness!", name));
    }

    public Result exerciseOfTheDay() {
        return ok(views.html.exerciseoftheday.render(new Exercise("Swimming", 60)));
    }

    public Result workoutOfTheDay() {
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new Exercise("Running Sprints", 10));
        exercises.add(new Exercise("Running Light Jog", 20));
        exercises.add(new Exercise("Running Sprints", 10));
        exercises.add(new Exercise("Cool Down", 10));
        return ok(views.html.workoutoftheday.render(exercises));
    }

    public Result initExercise() {
        Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        return ok(views.html.createexercise.render(exerciseForm));
    }


    @Transactional
    public Result createExercise() {
        Form<Exercise> filledInForm = formFactory.form(Exercise.class).bindFromRequest();
        if (filledInForm.hasErrors()) {
            return badRequest(views.html.createexercise.render(filledInForm));
        }
        Exercise exercise = filledInForm.get();
        exercise.save();
        cache.remove("exercise-list");
        return getList();
    }

    @Cached(key = "exercise-list", duration = 60)
    public Result getList() {
       return ok(views.html.allexercises.render(Exercise.find.all()));
    }

    public Result getJsonList() throws JsonProcessingException {
        return ok(Json.toJson(Exercise.find.all()));
    }

    public Result getXMLList() {

        StringBuilder sb = new StringBuilder();
        sb.append("<exercises>\n");
        for (Exercise exercise : Exercise.find.all()) {
            sb.append(
                    String.format("<exercise name=\"%s\"" +
                            " minutes=\"%d\" />\n",
                            exercise.getName(),
                            exercise.getMinutes()));
        }
        sb.append("</exercises>");
        return ok(sb.toString()).as("application/xml");
    }
}
