package com.donbosco.giancarlo.focusedsingularity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by GianCarlo on 3/2/2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class PomodoroTimerTest {


    public class BasicSetUpContext {
        @Test
        public void ItCanSetItsTaskToWorkOn() {
            PomodoroTimer timer = new PomodoroTimer();

            int timeEstimateInSeconds = 25000;
            timer.setTask(new PomodoroTask("Programming", timeEstimateInSeconds));

            assertThat(timer.getSelectedTask(), is(notNullValue()));
        }

        @Test
        public void ItCanHavePomodoroDurationAndBreakDurationSet() {
            PomodoroTimer timer = new PomodoroTimer();

            long pomodoroDuration = 8000L;
            long breakDuration = 5000L;
            timer.setTimerSettings(pomodoroDuration, breakDuration);

            assertThat(timer.getPomodoroDuration(), is(8000L));
            assertThat(timer.getBreakDuration(), is(5000L));
        }
    }

    public class TimerToTimerStateInteractionContext {

        private PomodoroTimer timer;
        private StateSpy stateSpy;

        @Before
        public void setUp() {
            timer = new PomodoroTimer() {
                @Override
                protected void sleep(int timeOut) {
                }

            };
            stateSpy = new StateSpy();
            timer.setState(stateSpy);
        }

        @Test
        public void ItShouldCallStateStartOnTimerStart() {
            timer.start();

            assertThat(stateSpy.startWasCalled, is(true));
        }
    }

    public class TimerToTaskInteractionContext {

        private PomodoroTimer timer;
        private TaskSpy taskSpy;

        @Before
        public void setUp() {
            timer = new PomodoroTimer() {
                @Override
                protected void sleep(int timeOut) {

                }
            };
            taskSpy = new TaskSpy();

            timer.setTask(taskSpy);

            long pomodoroDuration = 6000L;
            long breakDuration = 3000L;

            timer.setTimerSettings(pomodoroDuration, breakDuration);
        }

        @Test
        public void ItShouldBeAbleToCallTick6Times() throws Exception {
            timer.start();

            int expectedTickCalls = 6;
            assertThat(taskSpy.numberOfTickCalls, is(expectedTickCalls));
        }
    }

    public class AddingTimeToTaskContext {
        @Test
        public void ItShouldAddTimeToATaskBasedOnPomodoroDuration() throws Exception {
            PomodoroTimer timer = new PomodoroTimer() {
                @Override
                protected void sleep(int timeOut) {

                }
            };
            long timeEstimateInSeconds = 8000L;

            Task task = new PomodoroTask("Programming", timeEstimateInSeconds);

            timer.setTask(task);

            long pomodoroDuration = 4000L;
            long breakDuration = 0L;

            timer.setTimerSettings(pomodoroDuration, breakDuration);

            timer.start();

            long expectedTimeSpent = 4000L;

            assertThat(task.getTimeSpent(), is(expectedTimeSpent));
        }
    }

    public class InitialTickingContext {

        String sequence = "";
        PomodoroTimer timer;

        @Before
        public void WithATimerSetTo4Seconds() {
            timer = new PomodoroTimer() {
                @Override
                protected void tick() {
                    sequence += "T";
                }

                @Override
                protected void sleep(int timeOut) {
                    sequence += "s";
                }

            };

            timer.setTask(new TaskDummy());

            long pomodoroDuration = 4000L;
            long breakDuration = 0L;
            timer.setTimerSettings(pomodoroDuration, breakDuration);
        }

        @Test
        public void ItShouldPerformAWorkingCountDownOnStart() throws Exception {
            timer.start();

            assertThat(sequence, is("TsTsTsTs"));
        }
    }

    public class BreakTickingContext {

        private PomodoroTimer timer;

        String sequence = "";

        @Before
        public void WithATimerThatHasAlreadyPerformedItsWorkingCountDown() {
            timer = new PomodoroTimer() {
                @Override
                protected void sleep(int timeOut) {
                    sequence += "s";
                }

            };

            long breakDuration = 4000L;
            long pomodoroDuration = 0L;
            timer.setTimerSettings(pomodoroDuration, breakDuration);

            timer.start();
        }

        @Test
        public void ItShouldPerformBreakCountDownOn2ndStart() {
            timer.start();

            assertThat(sequence, is("ssss"));
        }

    }

}