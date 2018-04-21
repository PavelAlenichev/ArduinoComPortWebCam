package wcinterface.logic;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import wcinterface.gui.Console;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadComPort {  /*Класс чтения из порта*/

    private static SerialPort serialPort; /*Создаем объект типа SerialPort*/

    private static Console console;
    private static Listener listener;

    public static void setListener(Listener listener) {
        ReadComPort.listener = listener;
    }

    public static void setConsole(Console console) {
        ReadComPort.console = console;
    }

    public static void init() {  /* Точка входа в программу*/
        for (int portCounter = 0; portCounter < 20; portCounter++) {
            serialPort = new SerialPort("COM" + portCounter); /*Передаем в конструктор суперкласса имя порта с которым будем работать*/
            try {
                serialPort.openPort(); /*Метод открытия порта*/
                serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE); /*Задаем основные параметры протокола UART*/
                serialPort.setEventsMask(SerialPort.MASK_RXCHAR); /*Устанавливаем маску или список события на которые будет происходить реакция. В данном случае это приход данных в буффер порта*/
                serialPort.addEventListener(new EventListener()); /*Передаем экземпляр класса EventListener порту, где будет обрабатываться события. Ниже описан класс*/
                log("COM" + portCounter + " " + "attached!");
                break;
            } catch (SerialPortException ex) {
                logError("COM" + portCounter + " " + ex.getExceptionType());
            }
        }
    }

    public static void detach() {
        if (serialPort != null) {
            try {
                serialPort.closePort();
                log("Port detached!");
            } catch (SerialPortException e) {
                logError(e.getExceptionType());
            }
        }

    }

    private static class EventListener implements SerialPortEventListener { /*Слушатель срабатывающий по появлению данных на COM-порте*/

        private static int count = 0;

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) { /*Если происходит событие установленной маски и количество байтов в буфере более 0*/
                try {
                    String data = serialPort.readString(event.getEventValue()); /*Создаем строковую переменную  data, куда и сохраняем данные*/
                    log(data);/*Выводим данные на консоль*/
                    if (data.trim().equals("Get ready for shot!")) {
                        doPhoto(initDimens());
                    } else {
                        doNothing();
                    }
                } catch (SerialPortException ex) {
                    logError(ex.getExceptionType());
                }
            }
        }

        private Dimension[] initDimens() {
            return new Dimension[]{
                    WebcamResolution.PAL.getSize(),
                    WebcamResolution.HD.getSize(),
                    new Dimension(2000, 1000),
                    new Dimension(1000, 500),
            };
        }

        private void doPhoto(Dimension[] nonStandardResolutions) {
            Webcam webcam = Webcam.getDefault();
            webcam.setCustomViewSizes(nonStandardResolutions);
            webcam.setViewSize(WebcamResolution.HD.getSize());
            webcam.open();
            BufferedImage image = webcam.getImage();
            count++;
            try {
                ImageIO.write(image, "PNG", new File(count + ".png"));
                log("saved as " + count + ".png");
            } catch (IOException e) {
                logError(e.getMessage());
            }
            if (listener != null) {
                listener.onPhotoDone(count);
            }
            webcam.close();
        }

        private void doNothing() {
            log("I'm waiting!");
        }
    }

    private static void log(String message) {
        //System.out.println(message);
        console.println(message);
    }

    private static void logError(String message) {
        //System.out.println(message);
        console.printlnError(message);
    }

    public interface Listener {
        void onPhotoDone(int count);
    }
}