package org.example;

import org.example.jdm.app.ConsoleApplication;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        int status = new ConsoleApplication().run(args, System.out, System.err);
        if (status != 0) System.exit(status);
    }
}
