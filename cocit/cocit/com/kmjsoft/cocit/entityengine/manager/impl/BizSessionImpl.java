package com.kmjsoft.cocit.entityengine.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.jiongsoft.cocit.lang.Ex;
import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;
import com.jiongsoft.cocit.log.db.RunningLogDao;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.actionplugin.ActionEvent;
import com.kmjsoft.cocit.entity.actionplugin.IActionPlugin;
import com.kmjsoft.cocit.entity.impl.log.RunningLog;
import com.kmjsoft.cocit.entityengine.manager.IBizSession;
import com.kmjsoft.cocit.orm.ExtOrm;
import com.kmjsoft.cocit.orm.NoTransConnCallback;
import com.kmjsoft.cocit.orm.OrmCallback;
import com.kmjsoft.cocit.orm.Pager;
import com.kmjsoft.cocit.orm.expr.CndExpr;
import com.kmjsoft.cocit.orm.expr.NullCndExpr;

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

	private BizSessionImpl(BizSessionImpl parent, ExtOrm o) {
		actionQueue = parent.actionQueue;
	}

	@Override
	public IBizSession me() {
		return this;
	}

	@Override
	public IBizSession me(final ExtOrm orm) {
		class OrmBizSession extends BizSessionImpl {
			OrmBizSession() {
				super(BizSessionImpl.this, orm);
			}

			public ExtOrm orm() {
				return orm;
			}
		}
		return new OrmBizSession();
	}

	public ExtOrm orm() {
		return Demsy.orm();
	}

	protected ActionEvent createEvent() {
		ActionEvent e = new ActionEvent();
		e.setOrm(orm());

		return e;
	}

	private void fireLoadEvent(IActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (IActionPlugin l : plugins) {
				l.load(event);
			}
		}
	}

	private void fireLoadedEvent(IActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (IActionPlugin l : plugins) {
				l.loaded(event);
			}
		}
	}

	private void fireBeforeEvent(IActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (IActionPlugin l : plugins) {
				l.before(event);
			}
		}
	}

	private void fireAfterEvent(IActionPlugin[] plugins, ActionEvent event) {
		if (plugins != null) {
			for (IActionPlugin l : plugins) {
				l.after(event);
			}
		}
	}

	// ========================================================================
	// 同步实体管理
	// ========================================================================

	@Override
	public int save(Object obj, IActionPlugin... plugins) {
		return save(obj, null, plugins);
	}

	@Override
	public int save(final Object obj, final CndExpr fieldRexpr, final IActionPlugin... plugins) {
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
	public int updateMore(final Object obj, final CndExpr expr, final IActionPlugin... plugins) {
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
	public int delete(final Object entity, final IActionPlugin... plugins) {
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
	public int delete(final Class klass, final Long id, final IActionPlugin... plugins) {
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
	public int deleteMore(final Class klass, final CndExpr expr, final IActionPlugin... plugins) {
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
	public Object load(Class klass, Long id, IActionPlugin... plugins) {
		return this.load(klass, id, null, plugins);
	}

	@Override
	public Object load(final Class klass, final Long id, final CndExpr fieldRexpr, final IActionPlugin... plugins) {
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
	public Object load(final Class klass, final CndExpr expr, final IActionPlugin... plugins) {
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
	public int count(final Class klass, final CndExpr expr, final IActionPlugin... plugins) {
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
	public List query(final Class klass, final CndExpr expr, final IActionPlugin... plugins) {
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
	public List query(final Pager pager, final IActionPlugin... plugins) {
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
	public Object run(final Object obj, final CndExpr expr, final IActionPlugin... plugins) {
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
	public Object asynRun(Object obj, CndExpr fieldRexpr, IActionPlugin... plugins) {
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
	public void asynSave(Object obj, IActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		put(new DelayAction(ActionType.save, event, plugins));
	}

	@Override
	public void asynSave(Object obj, CndExpr fieldRexpr, IActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		event.setExpr(fieldRexpr);
		put(new DelayAction(ActionType.save, event, plugins));
	}

	@Override
	public void asynUpdateMore(Object obj, CndExpr expr, IActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		event.setExpr(expr);
		put(new DelayAction(ActionType.updateMore, event, plugins));
	}

	@Override
	public void asynDelete(Object obj, IActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntity(obj);
		put(new DelayAction(ActionType.delete, event, plugins));
	}

	@Override
	public void asynDeleteMore(Class klass, CndExpr expr, IActionPlugin... plugins) {
		ActionEvent event = createEvent();
		event.setEntityType(klass);
		event.setExpr(expr);
		put(new DelayAction(ActionType.deleteMore, event, plugins));
	}

	@Override
	public void asynQuery(Pager pager, IActionPlugin... plugins) {
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

		IActionPlugin[] plugins;

		private DelayAction(ActionType type, ActionEvent event, IActionPlugin... plugins) {
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
