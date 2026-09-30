package org.example.jdm.product.ecu;

import org.example.jdm.family.NissanR34Family;
public final class NissanR34ECU extends CalibratedECU<NissanR34Family> {
    public NissanR34ECU() { super("RB26DETT twin-turbo inline-six", 280, 180, 1.6, 0.002); }
}
