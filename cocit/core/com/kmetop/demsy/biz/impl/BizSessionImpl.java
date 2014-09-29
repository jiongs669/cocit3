package com.kmetop.demsy.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.jiongsoft.cocit.entity.ActionEvent;
import com.jiongsoft.cocit.entity.ActionPlugin;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.NullCndExpr;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.IBizSession;
import com.kmetop.demsy.comlib.impl.base.log.RunningLog;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.log.db.RunningLogDao;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.OrmCallback;
import com.kmetop.demsy.orm.Pager;

/**
 * 业务管理器——实体管理器
 * <p>
 * 主要实现事务控制的实体管理
 * 
 * @author yongshan.ji
 */
public class BizSessionImpl implements IBizSession {
	protected static Log log = Logs.getLog(BizSessionImpl.class);

	// 延迟操作队列
	private Queue<DelayAction> actionQueue;

	public BizSessionImpl() {
		actionQueue = new LinkedBlockingQueue();
		new ExecuteActionThread(actionQueue).start();
	}

	private BizSessionImpl(BizSessionImpl parent, IOrm o) {
		actionQueue = parent.actionQueue;
	}

	@Override
	public IBizSession me() {
		return this;
	}

	@Override
	public IBizSession me(final IOrm orm) {
		class OrmBizSession extends BizSessionImpl {
			OrmBizSession() {
				super(BizSessionImpl.this, orm);
			}

			public IOrm orm() {
				return orm;
			}
		}
		return new OrmBizSession();
	}

	public IOrm orm() {
		return Demsy.orm();
	}

	protected ActionEvent createEvent() {
		ActionEvent e = new ActionEvent();
		e.setOrm(orm());

		return e;
	}

	private void fireLoadEvent(ActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (ActionPlugin l : plugins) {
				l.load(event);
			}
		}
	}

	private void fireLoadedEvent(ActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (ActionPlugin l : plugins) {
				l.loaded(event);
			}
		}
	}

	private void fireBeforeEvent(ActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (ActionPlugin l : plugins) {
				l.before(event);
			}
		}
	}

	private void fireAfterEvent(ActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (ActionPlugin l : plugins) {
				l.after(event);
			}
		}
	}

	// ========================================================================
	// 同步实体管理
	// ========================================================================

	@Override
	public int save(Object obj, ActionPlugin... plugins) {
		return save(obj, null, plugins);
	}

	@Override
	public int save(final Object obj, final CndExpr fieldRexpr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntity(obj);
				event.setExpr(fieldRexpr);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm().save(event.getEntity(), null));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	@Override
	public int updateMore(final Object obj, final CndExpr expr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntity(obj);
				event.setExpr(expr);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm().updateMore(event.getEntity(), (CndExpr) event.getExpr()));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	@Override
	public int delete(final Object entity, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntity(entity);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm().delete(entity));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	@Override
	public int delete(final Class klass, final Long id, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntityType(klass);
				event.setEntityID(id);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm().delete(event.getEntityType(), event.getEntityID()));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	@Override
	public int deleteMore(final Class klass, final CndExpr expr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntityType(klass);
				event.setExpr(expr);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm().deleteMore(event.getEntityType(), (CndExpr) event.getExpr()));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	@Override
	public Object load(Class klass, Long id, ActionPlugin... plugins) {
		return this.load(klass, id, null, plugins);
	}

	@Override
	public Object load(final Class klass, final Long id, final CndExpr fieldRexpr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntityType(klass);
				event.setExpr(fieldRexpr);
				event.setEntityID(id);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm().load(event.getEntityType(), event.getEntityID(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return event.getReturnValue();
	}

	@Override
	public Object load(final Class klass, final CndExpr expr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setExpr(expr);
				event.setEntityType(klass);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm().load(event.getEntityType(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return event.getReturnValue();
	}

	@Override
	public int count(final Class klass, final CndExpr expr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setExpr(expr);
				event.setEntityType(klass);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm().count(event.getEntityType(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	@Override
	public List query(final Class klass, final CndExpr expr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntityType(klass);
				event.setExpr(expr);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm().query(event.getEntityType(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return (List) event.getReturnValue();
	}

	@Override
	public List query(final Pager pager, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntity(pager);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm().query(pager));

				fireLoadedEvent(plugins, event);
			}
		});
		return (List) event.getReturnValue();
	}

	@Override
	public Object run(final Object obj, final CndExpr expr, final ActionPlugin... plugins) {
		final ActionEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setEntity(obj);
				event.setExpr(expr);
				fireBeforeEvent(plugins, event);
				// noop
				fireAfterEvent(plugins, event);
			}
		});
		return event.getReturnValue();
	}

	@Override
	public Object run(final OrmCallback callback) {
		final List ret = new ArrayList();
		Trans.exec(new Atom() {
			public void run() {
				ret.add(callback.invoke(orm()));
			}
		});
		return ret.get(0);
	}

	@Override
	public Object run(final NoTransConnCallback conn) {
		final List ret = new ArrayList();
		Trans.exec(new Atom() {
			public void run() {
				ret.add(orm().run(conn));
			}
		});
		return ret.get(0);
	}

	@Override
	public Object asynRun(Object obj, CndExpr fieldRexpr, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		event.setExpr(fieldRexpr);
		put(new DelayAction(ActionType.run, event, plugins));

		return event.getReturnValue();
	}

	// ========================================================================
	// 异步实体管理
	// ========================================================================

	@Override
	public void asynSave(Object obj, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		put(new DelayAction(ActionType.save, event, plugins));
	}

	@Override
	public void asynSave(Object obj, CndExpr fieldRexpr, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		event.setExpr(fieldRexpr);
		put(new DelayAction(ActionType.save, event, plugins));
	}

	@Override
	public void asynUpdateMore(Object obj, CndExpr expr, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		event.setExpr(expr);
		put(new DelayAction(ActionType.updateMore, event, plugins));
	}

	@Override
	public void asynDelete(Object obj, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		put(new DelayAction(ActionType.delete, event, plugins));
	}

	@Override
	public void asynDeleteMore(Class klass, CndExpr expr, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntityType(klass);
		event.setExpr(expr);
		put(new DelayAction(ActionType.deleteMore, event, plugins));
	}

	@Override
	public void asynQuery(Pager pager, ActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(pager);
		put(new DelayAction(ActionType.query, event, plugins));
	}

	private void put(DelayAction action) {
		if (action == null) {
			return;
		}
		synchronized (actionQueue) {
			int size = actionQueue.size();
			actionQueue.add(action);
			if (size == 0) {
				actionQueue.notifyAll();
			}
		}
	}

	private enum ActionType {
		save, updateMore, delete, deleteMore, query, run;
	}

	private static class DelayAction {
		ActionType type;

		ActionEvent event;

		ActionPlugin[] plugins;

		private DelayAction(ActionType type, ActionEvent event, ActionPlugin... plugins) {
			this.type = type;
			this.event = event;
			this.plugins = plugins;
		}

	}

	private class ExecuteActionThread extends Thread {
		private Queue<DelayAction> actionQueue;

		ExecuteActionThread(Queue<DelayAction> queue) {
			this.actionQueue = queue;
		}

		protected void execute(DelayAction action) throws Exception {
			ActionEvent event = action.event;
			if (action.type == ActionType.save) {
				if (event.getEntity() instanceof RunningLog) {
					RunningLogDao.me().save((RunningLog) event.getEntity());
					return;
				}
				BizSessionImpl.this.save((Object) event.getEntity(), (NullCndExpr) event.getExpr(), action.plugins);
			} else if (action.type == ActionType.updateMore) {
				BizSessionImpl.this.updateMore(event.getEntity(), (CndExpr) event.getExpr(), action.plugins);
			} else if (action.type == ActionType.delete) {
				BizSessionImpl.this.delete(event.getEntity(), action.plugins);
			} else if (action.type == ActionType.deleteMore) {
				BizSessionImpl.this.deleteMore(event.getEntityType(), (CndExpr) event.getExpr(), action.plugins);
			} else if (action.type == ActionType.query) {
				BizSessionImpl.this.query((Pager) event.getEntity(), action.plugins);
			} else if (action.type == ActionType.run) {
				BizSessionImpl.this.run(event.getEntity(), (CndExpr) event.getExpr(), action.plugins);
			}
		}

		@Override
		public void run() {
			super.run();
			while (true) {
				synchronized (actionQueue) {
					if (actionQueue.size() == 0) {
						try {
							actionQueue.wait();
						} catch (InterruptedException e) {
							log.errorf("异步BizSession: 任务队列等待出错! %s", Ex.msg(e));
							break;
						}
					}
				}
				if (actionQueue.size() == 0) {
					continue;
				}
				DelayAction action = actionQueue.poll();
				try {
					execute(action);
				} catch (Throwable e) {
					Object obj = action.event.getEntity();
					if (obj instanceof RunningLog) {
						System.err.println("异步BizSession: 执行任务出错! [action=" + action + ", obj=" + obj + "] " + e);
					} else {
						log.errorf("异步BizSession: 执行任务出错! [action=%s, obj=%s] %s ", action, obj, e);
					}
				}
			}
		}
	}
}
