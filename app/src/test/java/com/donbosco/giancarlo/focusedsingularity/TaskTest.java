package com.donbosco.giancarlo.focusedsingularity;


import org.hamcrest.CoreMatchers;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by GianCarlo on 3/9/2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class TaskTest {


    public class ATask {

        Task task;

        @Before
        public void WithA3SecondEstimateAnd2SecondPomodoroDurationShould() {
            task = new PomodoroTask("Testing", 3000L) {
                @Override
                protected void sleep(int timeOut) {
                }
            };

            task.setPomodoroDuration(2000L);
        }

        @Test
        public void HaveItsCurrentStateToWorkingOnCreation() throws Exception {
            TaskState expectedState = PomodoroTaskState.WORKING;

            assertThat(task.getCurrentState(), is(expectedState));
        }

        @Test
        public void AddTimeBasedOnSetPomodoroDuration() {
            task.start();

            assertThat(task.getTimeSpent(), is(2000L));
        }

        @Test
        public void TransitionToBreakStateAfterCompletingStart() {
            task.start();

            TaskState expectedState = PomodoroTaskState.BREAK;
            assertThat(task.getCurrentState(), is(expectedState));
        }


        // Change Reference to another class, maybe a Fake for the Task

        // Test for on task start transition to working then break;

    }

    public class TaskStartedContext {


        private Task task;

        @Before
        public void WithATaskThatIsStarted() throws Exception {
            task = new PomodoroTask("Typing", 4000L) {
                @Override
                protected void sleep(int timeOut) {
                }
            };

            task.start();
        }

        @Test
        public void ItShouldBeInTheStoppedStateOnStop() throws Exception {
            task.stop();

            TaskState expectedState = PomodoroTaskState.STOPPED;
            assertThat(task.getCurrentState(), is(expectedState));
        }
    }

    public class TaskStoppedContext {

        private Task task;

        @Before
        public void WithATaskThatIsStopped() throws Exception {
            task = new PomodoroTask("Rolling", 6000L) {
                @Override
                protected void sleep(int timeOut) {
                }
            };

            task.stop();
        }

        @Test
        public void ItShouldNotAddTimeSpentWhenStartingAgain() {
            task.start();

            assertThat(task.getTimeSpent(), is(0L));
        }

        @Test
        public void ItShouldBeInWorkingStateAgainOnReset(){
            task.reset();

            TaskState expectedState = PomodoroTaskState.WORKING;
            assertThat(task.getCurrentState(), is(expectedState));
        }
    }
}
