package edu.pdx.cs410J.jsl.client;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ReflectionHelper;

@SuppressWarnings("deprecation")
public class AppointmentBook_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.Collection getAppts(edu.pdx.cs410J.jsl.client.AppointmentBook instance) /*-{
    return instance.@edu.pdx.cs410J.jsl.client.AppointmentBook::appts;
  }-*/;
  
  private static native void setAppts(edu.pdx.cs410J.jsl.client.AppointmentBook instance, java.util.Collection value) 
  /*-{
    instance.@edu.pdx.cs410J.jsl.client.AppointmentBook::appts = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, edu.pdx.cs410J.jsl.client.AppointmentBook instance) throws SerializationException {
    setAppts(instance, (java.util.Collection) streamReader.readObject());
    
    edu.pdx.cs410J.AbstractAppointmentBook_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static edu.pdx.cs410J.jsl.client.AppointmentBook instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new edu.pdx.cs410J.jsl.client.AppointmentBook();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, edu.pdx.cs410J.jsl.client.AppointmentBook instance) throws SerializationException {
    streamWriter.writeObject(getAppts(instance));
    
    edu.pdx.cs410J.AbstractAppointmentBook_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return edu.pdx.cs410J.jsl.client.AppointmentBook_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    edu.pdx.cs410J.jsl.client.AppointmentBook_FieldSerializer.deserialize(reader, (edu.pdx.cs410J.jsl.client.AppointmentBook)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    edu.pdx.cs410J.jsl.client.AppointmentBook_FieldSerializer.serialize(writer, (edu.pdx.cs410J.jsl.client.AppointmentBook)object);
  }
  
}
