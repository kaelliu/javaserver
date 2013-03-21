package lib.kael.util;

import java.lang.ref.Reference;
import java.lang.reflect.*;
/**
* ���߳����ThreadLocalMap
*/
public class ThreadLocalCleanUtil {
	/**
	* �õ���ǰ�߳����е������߳� description:
	* 
	* @return
	*/
	private static Thread[] getThreads() {
	   ThreadGroup tg = Thread.currentThread().getThreadGroup();

	   while (tg.getParent() != null) {
	    tg = tg.getParent();
	   }

	   int threadCountGuess = tg.activeCount() + 50;
	   Thread[] threads = new Thread[threadCountGuess];
	   int threadCountActual = tg.enumerate(threads);

	   while (threadCountActual == threadCountGuess) {
	    threadCountGuess *= 2;
	    threads = new Thread[threadCountGuess];

	    threadCountActual = tg.enumerate(threads);
	   }

	   return threads;
	}
	
	public static void clearThreadLocals() {
	   ClassLoader classloader = Thread
	     .currentThread()
	     .getContextClassLoader();

	   Thread[] threads = getThreads();
	   try {
	    java.lang.reflect.Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");

	    threadLocalsField.setAccessible(true);
	    java.lang.reflect.Field inheritableThreadLocalsField = Thread.class.getDeclaredField("inheritableThreadLocals");

	    inheritableThreadLocalsField.setAccessible(true);

	    Class tlmClass = Class
	      .forName("java.lang.ThreadLocal$ThreadLocalMap");

	    java.lang.reflect.Field tableField = tlmClass.getDeclaredField("table");
	    tableField.setAccessible(true);

	    for (int i = 0; i < threads.length; ++i) {
	     if (threads[i] == null)
	      continue;
	     Object threadLocalMap = threadLocalsField.get(threads[i]);
	     clearThreadLocalMap(threadLocalMap, tableField, classloader);

	     threadLocalMap = inheritableThreadLocalsField.get(threads[i]);

	     clearThreadLocalMap(threadLocalMap, tableField, classloader);
	    }
	   } catch (Exception e) {

	    e.printStackTrace();
	   }
	}
	
	private static void clearThreadLocalMap(Object map,
		java.lang.reflect.Field internalTableField, ClassLoader classloader)
	    throws NoSuchMethodException, IllegalAccessException,
	    NoSuchFieldException, InvocationTargetException {
	   if (map != null) {
	    Method mapRemove = map.getClass().getDeclaredMethod("remove",
	      new Class[] { ThreadLocal.class });

	    mapRemove.setAccessible(true);
	    Object[] table = (Object[]) internalTableField.get(map);
	    int staleEntriesCount = 0;
	    if (table != null) {
	     for (int j = 0; j < table.length; ++j) {
	      if (table[j] != null) {
	       boolean remove = false;

	       Object key = ((Reference) table[j]).get();
	       if ((key != null)
	         && (key.getClass().getClassLoader() == classloader)) {
	        remove = true;

	        System.out.println("clean threadLocal key,class="
	          + key.getClass().getCanonicalName()
	          + ",value=" + key.toString());
	       }

	       java.lang.reflect.Field valueField = table[j]
	         .getClass()
	         .getDeclaredField("value");

	       valueField.setAccessible(true);
	       Object value = valueField.get(table[j]);

	       if ((value != null)
	         && (value.getClass().getClassLoader() == classloader)) {
	        remove = true;
	        System.out.println("clean threadLocal value,class="
	          + value.getClass().getCanonicalName()
	          + ",value=" + value.toString());

	       }

	       if (remove) {

	        if (key == null)
	         ++staleEntriesCount;
	        else {
	         mapRemove.invoke(map, new Object[] { key });
	        }
	       }
	      }
	     }
	    }
	    if (staleEntriesCount > 0) {
	     Method mapRemoveStale = map
	       .getClass()
	       .getDeclaredMethod("expungeStaleEntries", new Class[0]);

	     mapRemoveStale.setAccessible(true);
	     mapRemoveStale.invoke(map, new Object[0]);
	    }
	   }
	}
}
