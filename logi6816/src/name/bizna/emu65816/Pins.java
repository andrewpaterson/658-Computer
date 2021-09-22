package name.bizna.emu65816;

public class Pins
{
  // Reset to true means low power mode (do nothing) (should jump indirect via 0x00FFFC)
  boolean RES = true;

  // Ready to false means CPU is waiting for an NMI/IRQ/ABORT/RESET
  boolean RDY = false;

  // nmi true execute nmi vector (0x00FFEA)
  boolean NMI = false;

  // irq true exucute irq vector (0x00FFEE)
  boolean IRQ = false;

  // abort true execute abort vector (0x00FFE8)
  boolean ABORT = false;
}

