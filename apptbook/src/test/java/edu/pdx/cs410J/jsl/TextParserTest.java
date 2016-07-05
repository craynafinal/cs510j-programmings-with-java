package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.ParserException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TextParserTest {
    static final String filename = "file.txt";
    static final String owner = "owner";
    TextParser textParser = null;

    @Before
    public void appointmentSetup() {
        textParser = new TextParser(filename, owner);
    }

    @Test
    public void shouldSetFileNameCorrectly() {
        assertThat(textParser.getFileName(), is(equalTo(filename)));
    }

    @Test
    public void test3() {
        AppointmentBook ab = new AppointmentBook("owner");
        ab.addAppointment(new Appointment("desc1", "time1", "time1"));
        ab.addAppointment(new Appointment("desc2", "time2", "time2"));

        TextDumper textDumper = new TextDumper("/home/crayna/Downloads/test111.txt");
        try {
            textDumper.dump(ab);
        } catch (Exception e) {

        }

        AppointmentBook ab2 = null;

        textParser = new TextParser("/home/crayna/Downloads/test111.txt", owner);
        try {
            // a way to make it not to cast?
            ab2 = (AppointmentBook)textParser.parse();
        } catch (Exception e) {

        }
        System.out.println(ab2.getAppointments());
    }

    @Test
    public void test() {
        textParser = new TextParser("/home/crayna/Downloads/test.txt", owner);
        /*
        try {
            textParser.parse();
        } catch (ParserException e) {
            fail("parse fail");
        }*/

        //System.out.println(textParser.testFunction().length);
        try {
            textParser.parse();
        } catch (Exception e) {

        }

/*
        File file = new File(filename);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file);

            document.getDocumentElement().normalize();

            System.out.println(document.getDocumentElement().getNodeName()); // should be appointmentbook

            //document.getElementsByTagName()

        } catch (Exception e) {

        }*/
    }

    @Test
    public void test2() {
        AppointmentBook ab = new AppointmentBook("owner");
        ab.addAppointment(new Appointment("desc", "time1", "time2"));

        try {
            FileOutputStream fileout = new FileOutputStream("/home/crayna/Downloads/temp.txt");
            ObjectOutputStream objout = new ObjectOutputStream(fileout);
            objout.writeObject(ab);
            objout.close();
            fileout.close();
        } catch (IOException e) {

        }

        AppointmentBook ab2 = null;

        try {
            FileInputStream filein = new FileInputStream("/home/crayna/Downloads/temp.txt");
            ObjectInputStream in = new ObjectInputStream(filein);
            ab2 = (AppointmentBook) in.readObject();
            in.close();
            filein.close();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }

        System.out.println(ab2.getOwnerName());
        System.out.println(ab2.getAppointments().get(0).getDescription());
    }
}
