package org.example.jdm.product.ecu;

import org.example.jdm.family.MazdaRX7Family;
public final class MazdaRX7ECU extends CalibratedECU<MazdaRX7Family> {
    public MazdaRX7ECU() { super("13B-REW twin-rotor twin-turbo", 255, 160, 1.1, 0.004); }
}
