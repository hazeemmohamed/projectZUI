package zeen.ui;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author M S I
 */
public class HandlingWindow {

    public interface StdCall extends StdCallLibrary {

        StdCall INSTANCE = (StdCall) Native.loadLibrary("user32", StdCall.class);

        boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);

        int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);
    }

    public static void printAllWindows() {
        final StdCall stdCall = StdCall.INSTANCE;
        stdCall.EnumWindows(new WinUser.WNDENUMPROC() {
            int count = 0;

            @Override
            public boolean callback(HWND hWnd, Pointer arg1) {
                byte[] windowText = new byte[512];
                stdCall.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                // get rid of this if block if you want all windows regardless of whether
                // or not they have text
                if (wText.isEmpty()) {
                    return true;
                }

                System.out.println("Found window with text " + hWnd + ", total " + ++count
                        + " Text: " + wText);
                return true;
            }
        }, null);
    }

    public static void main(String[] args) {
        // printAllWindows();
        User32 user = User32.INSTANCE;
        HWND hwnd = user.FindWindow(null, "Untitled - Notepad");
        System.out.println("window: " + hwnd);
        user.ShowWindow(hwnd, 9); 
     
        user.PostMessage(hwnd, WinUser.WM_CLOSE, null, null);
      // boolean pr= user.DestroyWindow(hwnd);
        //System.out.println(pr);
        user.SetForegroundWindow(hwnd);
        
    }

}
