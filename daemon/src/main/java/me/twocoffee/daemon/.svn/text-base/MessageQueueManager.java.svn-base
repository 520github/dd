package me.twocoffee.daemon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import me.twocoffee.common.mq.MessageQueueEntity;
import me.twocoffee.common.mq.MessageQueueSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import com.surftools.BeanstalkClient.BeanstalkException;
import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClient.Job;

public class MessageQueueManager {
    protected static final Logger LOGGER = LoggerFactory
	    .getLogger(MessageQueueManager.class);
    private List<MessageQueueDisposer> messageDisposers = null;
    private TaskExecutor taskExecutor = null;
    private Client queueClient = null;

    public MessageQueueManager(List<MessageQueueDisposer> mqDisposers,
	    TaskExecutor taskExecutor, Client queueClient) {
	messageDisposers = mqDisposers;
	this.taskExecutor = taskExecutor;
	this.queueClient = queueClient;
    }

    private boolean started = false;

    private Runnable runner = null;

    public void startServer() {
	LOGGER.warn("Message queue server started.");
	if (messageDisposers == null || messageDisposers.size() < 1)
	    return;

	started = true;

	runner = new Runnable() {
	    @Override
	    public void run() {
		while (started) {
		    Job job = null;
		    try {
			job = queueClient
				.reserve(MessageQueueSender.timeoutSeconds);
			queueClient.delete(job.getJobId());
			final Job j = job;

			// 得到数据，如非系统数据，则设置等待
			final MessageQueueEntity entity = unserializableForJob(j);
			if (entity == null) {
			    LOGGER.warn("Job entity is null,id is "
				    + j.getJobId());
			    queueClient.release(j.getJobId(),
				    MessageQueueSender.defaultPriority,
				    MessageQueueSender.delaySeconds);
			    return;
			}

			// 得到队列处理程序，如不能得到处理程序，则设置等待
			final MessageQueueDisposer d = findByName(
				messageDisposers, entity.getQueueName());
			if (d == null) {
			    LOGGER.warn("Queue disposer is null,id is "
				    + j.getJobId() + ",queue name is "
				    + entity.getQueueName());
			    queueClient.release(j.getJobId(),
				    MessageQueueSender.defaultPriority,
				    MessageQueueSender.delaySeconds);
			    return;
			}

			// 执行任务
			taskExecutor.execute(new Runnable() {

			    @Override
			    public void run() {
				LOGGER.info("Executing job,id is "
					+ j.getJobId() + ",queue name is "
					+ entity.getQueueName());
				// 处理任务，失败则挂起
				if (d.dispose(entity.getData())) {
				    LOGGER.info("Job execute success,id is "
					    + j.getJobId() + ",queue name is "
					    + entity.getQueueName());

				} else {
				    LOGGER.warn("Job execute fault,id is "
					    + j.getJobId() + ",queue name is "
					    + entity.getQueueName());

				    DuoduoQueueRetryPolicy policy = new DuoduoQueueRetryPolicy(
					    2);

				    boolean success = false;

				    while (policy.needRetry()) {
					LOGGER.info("Retry job,id is "
						+ j.getJobId()
						+ ",queue name is "
						+ entity.getQueueName());

					if (d.dispose(entity.getData())) {
					    LOGGER.info("Job execute success,id is "
						    + j.getJobId()
						    + ",queue name is "
						    + entity.getQueueName());

					    success = true;
					    break;

					} else {
					    LOGGER.warn("Job execute fault,id is "
						    + j.getJobId()
						    + ",queue name is "
						    + entity.getQueueName());
					}
				    }

				    if (!success) {
					queueClient.bury(j.getJobId(), 65535);
				    }
				}
			    }
			});

			Thread.sleep(10);
		    }
		    catch (BeanstalkException be) {
			LOGGER.error("Beanstalked error.", be);
		    }
		    catch (Exception e) {
			LOGGER.error("Job running error.", e);

			try {

			    if (job != null) {
				queueClient.bury(job.getJobId(), 65535);
			    }

			} catch (Exception e1) {
			    LOGGER.error(
				    "Job running error.can not bury job {}",
				    job.getJobId());

			}
		    }
		}
	    }
	};
	taskExecutor.execute(runner);
    }

    private MessageQueueDisposer findByName(
	    List<MessageQueueDisposer> messageDisposers,
	    String queueName) {
	if (messageDisposers == null || messageDisposers.size() < 1)
	    return null;
	for (MessageQueueDisposer q : messageDisposers) {
	    if (q.getQueueName().equals(queueName))
		return q;
	}
	return null;
    }

    private MessageQueueEntity unserializableForJob(Job j) {
	ByteArrayInputStream bs = null;
	ObjectInputStream in = null;
	try {
	    bs = new ByteArrayInputStream(j.getData());
	    in = new ObjectInputStream(bs);

	    return (MessageQueueEntity) in.readObject();
	} catch (IOException e1) {
	    LOGGER.error("unserializableForJob error.", e1);
	    e1.printStackTrace();
	} catch (ClassNotFoundException ce) {
	    LOGGER.error("unserializableForJob error.", ce);
	    ce.printStackTrace();
	} finally {
	    try {
		in.close();
		bs.close();
	    } catch (IOException e1) {
		e1.printStackTrace();
		LOGGER.error("close stream error.", e1);
	    }
	}
	return null;
    }

    public void stopServer() {
	started = false;
	LOGGER.warn("Message queue server stopping.");

	if (runner != null) {
	    try {
		runner.wait(10000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
		LOGGER.error("wait server.", e);
	    }
	}
    }

    public static class DuoduoQueueRetryPolicy {

	private int max = 2;

	public DuoduoQueueRetryPolicy(int max) {
	    this.max = max;
	}

	boolean needRetry() {
	    return --max >= 0 ? true : false;
	}
    }
}
