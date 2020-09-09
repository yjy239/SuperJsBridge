package com.yjy.rnbridge.RnCompent;

import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.yjy.superbridge.internal.convert.ChildConvertFactory;

import java.util.Stack;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/09/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Transformer implements ChildConvertFactory {

    private static final String TAG = Transformer.class.getSimpleName();
    private Stack<Node> stack = new Stack<>();
    int depth = 0;
    Object result;
    @Override
    public void objectBegin(String name) {
        //Log.e(TAG,"--- objectBegin --"+name);
        WritableNativeMap tmp = new WritableNativeMap();
        stack.push(new Node(name,tmp));
        depth++;
    }

    @Override
    public void walk(String key, Object value) {
       // Log.e(TAG,"--- walk --"+" key:"+key+" value:"+value);
        Class clazz = null;
        if(value != null){
            clazz = value.getClass();
        }
        Node node = stack.peek();
        Object obj = node.value;

        if(obj instanceof WritableNativeMap){
            WritableNativeMap map = (WritableNativeMap)obj;
            if(clazz!=null){
                if (clazz == Boolean.class) {
                    map.putBoolean(key,(boolean)value);
                } else if (clazz == Integer.class) {
                    map.putInt(key,(Integer)value);
                } else if (clazz == Double.class) {
                    map.putDouble(key,(Double)value);
                } else if (clazz == Float.class) {
                    map.putDouble(key,(Double)value);
                } else if (clazz == String.class) {
                    map.putString(key,(String)value);
                }
            }else {
                map.putNull(key);
            }
        }else if(obj instanceof WritableNativeArray){
            WritableNativeArray array = (WritableNativeArray)obj;
            if(clazz!=null){
                if (clazz == Boolean.class) {
                    array.pushBoolean((boolean)value);
                } else if (clazz == Integer.class) {
                    array.pushInt((Integer)value);
                } else if (clazz == Double.class) {
                    array.pushDouble((Double)value);
                } else if (clazz == Float.class) {
                    array.pushDouble((Double)value);
                } else if (clazz == String.class) {
                    array.pushString((String)value);
                }
            }else {
                array.pushNull();
            }
        }

    }

    @Override
    public void objectEnd() {
        depth--;
        Node node = stack.pop();
        Object obj = node.value;
        if(stack.size() > 0){
            Node parent = stack.peek();
            if(parent != null&&obj instanceof WritableNativeMap){
                WritableNativeMap tmp = (WritableNativeMap)obj;
                if(parent.value instanceof WritableNativeMap){
                    ((WritableNativeMap) parent.value).putMap(node.name,tmp);
                }else if(parent.value instanceof WritableNativeArray){
                    ((WritableNativeArray) parent.value).pushMap(tmp);
                }
            }
        }

        if(depth == 0){
            result = obj;
        }
    }

    @Override
    public void arrayBegin(String name) {
        depth++;
        WritableNativeArray tmp = new WritableNativeArray();
        stack.push(new Node(name,tmp));
    }

    @Override
    public void arrayEnd() {
        depth--;
        //Log.e(TAG,"--- arrayEnd --"+depth);
        Node node = stack.pop();
        Object obj = node.value;
        if(stack.size() > 0){
            Node parent = stack.peek();
            if(parent != null&&obj instanceof WritableNativeArray){
                WritableNativeArray tmp = (WritableNativeArray)obj;
                if(parent.value instanceof WritableNativeMap){
                    ((WritableNativeMap) parent.value).putArray(node.name,tmp);
                }else if(parent.value instanceof WritableNativeArray){
                    ((WritableNativeArray) parent.value).pushArray(tmp);
                }
            }
        }

        if(depth == 0){
            result = obj;
        }

    }

    public Object getResult(){
        return result;
    }

    static class Node{
        String name;
        Object value;

        public Node(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
