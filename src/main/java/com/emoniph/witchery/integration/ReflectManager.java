package com.emoniph.witchery.integration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectManager {

   public static HashMap primitiveWrappers = new HashMap();


   public static boolean isInstance(Class class1, Object obj) {
      Class primitive = (Class)primitiveWrappers.get(class1);
      return primitive != null?(primitive == Long.class && Long.class.isInstance(obj)?true:((primitive == Long.class || primitive == Integer.class) && Integer.class.isInstance(obj)?true:((primitive == Long.class || primitive == Integer.class || primitive == Short.class) && Short.class.isInstance(obj)?true:((primitive == Long.class || primitive == Integer.class || primitive == Short.class || primitive == Byte.class) && Integer.class.isInstance(obj)?true:(primitive == Double.class && Double.class.isInstance(obj)?true:((primitive == Double.class || primitive == Float.class) && Float.class.isInstance(obj)?true:primitive.isInstance(obj))))))):class1.isInstance(obj);
   }

   public static Class findClass(String name) {
      return findClass(name, true);
   }

   public static boolean classExists(String name) {
      return findClass(name, false) != null;
   }

   public static Class findClass(String name, boolean init) {
      try {
         return Class.forName(name, init, ReflectManager.class.getClassLoader());
      } catch (ClassNotFoundException var5) {
         try {
            return Class.forName("net.minecraft.src." + name, init, ReflectManager.class.getClassLoader());
         } catch (ClassNotFoundException var4) {
            return null;
         }
      }
   }

   public static void setField(Class class1, Object instance, String name, Object value) throws IllegalArgumentException, IllegalAccessException {
      setField(class1, instance, new String[]{name}, value);
   }

   public static void setField(Class class1, Object instance, String[] names, Object value) throws IllegalArgumentException, IllegalAccessException {
      Field[] arr$ = class1.getDeclaredFields();
      int len$ = arr$.length;
      int i$ = 0;

      while(i$ < len$) {
         Field field = arr$[i$];
         boolean match = false;
         String[] arr$1 = names;
         int len$1 = names.length;
         int i$1 = 0;

         while(true) {
            if(i$1 < len$1) {
               String name = arr$1[i$1];
               if(!field.getName().equals(name)) {
                  ++i$1;
                  continue;
               }

               match = true;
            }

            if(match) {
               field.setAccessible(true);
               field.set(instance, value);
               return;
            }

            ++i$;
            break;
         }
      }

   }

   public static void setField(Class class1, Object instance, int fieldindex, Object value) throws IllegalArgumentException, IllegalAccessException {
      Field field = class1.getDeclaredFields()[fieldindex];
      field.setAccessible(true);
      field.set(instance, value);
   }

   public static void callMethod(Class class1, String name, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      callMethod(class1, (Class)null, new String[]{name}, params);
   }

   public static void callMethod(Class class1, String[] names, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      callMethod(class1, (Class)null, names, params);
   }

   public static void callMethod(Class class1, Object instance, String name, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      callMethod(class1, (Class)null, instance, new String[]{name}, params);
   }

   public static void callMethod(Class class1, Object instance, String[] names, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      callMethod(class1, (Class)null, instance, names, params);
   }

   public static Object callMethod(Class class1, Class returntype, String name, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      return callMethod(class1, returntype, (Object)null, new String[]{name}, params);
   }

   public static Object callMethod(Class class1, Class returntype, String[] names, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      return callMethod(class1, returntype, (Object)null, names, params);
   }

   public static Object callMethod(Class class1, Class returntype, Object instance, String name, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      return callMethod(class1, returntype, instance, new String[]{name}, params);
   }

   public static Object callMethod(Class class1, Class returntype, Object instance, String[] names, Object ... params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      Method[] arr$ = class1.getDeclaredMethods();
      int len$ = arr$.length;
      int i$ = 0;

      while(i$ < len$) {
         Method method = arr$[i$];
         boolean match = false;
         String[] paramtypes = names;
         int i = names.length;
         int i$1 = 0;

         while(true) {
            if(i$1 < i) {
               String name = paramtypes[i$1];
               if(!method.getName().equals(name)) {
                  ++i$1;
                  continue;
               }

               match = true;
            }

            if(match) {
               Class[] var14 = method.getParameterTypes();
               if(var14.length == params.length) {
                  i = 0;

                  while(true) {
                     if(i >= params.length) {
                        method.setAccessible(true);
                        return method.invoke(instance, params);
                     }

                     if(!isInstance(var14[i], params[i])) {
                        break;
                     }

                     ++i;
                  }
               }
            }

            ++i$;
            break;
         }
      }

      return null;
   }

   public static Object getField(Class class1, Class fieldType, Object instance, int fieldIndex) throws IllegalArgumentException, IllegalAccessException {
      Field field = class1.getDeclaredFields()[fieldIndex];
      field.setAccessible(true);
      return field.get(instance);
   }

   public static Object getField(Class class1, Class fieldType, Object instance, String fieldName) {
      try {
         Field e = class1.getDeclaredField(fieldName);
         e.setAccessible(true);
         return e.get(instance);
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }

   public static Object newInstance(Class class1, Object ... params) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Constructor[] arr$ = class1.getDeclaredConstructors();
      int len$ = arr$.length;

      label27:
      for(int i$ = 0; i$ < len$; ++i$) {
         Constructor constructor = arr$[i$];
         Class[] paramtypes = constructor.getParameterTypes();
         if(paramtypes.length == params.length) {
            for(int i = 0; i < params.length; ++i) {
               if(!isInstance(paramtypes[i], params[i])) {
                  continue label27;
               }
            }

            constructor.setAccessible(true);
            return constructor.newInstance(params);
         }
      }

      return null;
   }

   public static boolean hasField(Class class1, String fieldName) {
      try {
         class1.getDeclaredField(fieldName);
         return true;
      } catch (NoSuchFieldException var3) {
         return false;
      }
   }

   public static Object get(Field field, Class class1) {
      return get(field, class1, (Object)null);
   }

   public static Object get(Field field, Class class1, Object instance) {
      try {
         return field.get(instance);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public static void set(Field field, Object value) {
      set(field, (Object)null, value);
   }

   public static void set(Field field, Object instance, Object value) {
      try {
         field.set(instance, value);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   static {
      primitiveWrappers.put(Integer.TYPE, Integer.class);
      primitiveWrappers.put(Short.TYPE, Short.class);
      primitiveWrappers.put(Byte.TYPE, Byte.class);
      primitiveWrappers.put(Long.TYPE, Long.class);
      primitiveWrappers.put(Double.TYPE, Double.class);
      primitiveWrappers.put(Float.TYPE, Float.class);
      primitiveWrappers.put(Boolean.TYPE, Boolean.class);
      primitiveWrappers.put(Character.TYPE, Character.class);
   }
}
