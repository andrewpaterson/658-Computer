package name.bizna.emu65816;

public class EmulationModeInterrupts
{
  public int coProcessorEnable;
  public int unused;
  public int abort;
  public int nonMaskableInterrupt;
  public int reset;
  public int brkIrq;

  public EmulationModeInterrupts(int coProcessorEnable,
                                 int unused,
                                 int abort,
                                 int nonMaskableInterrupt,
                                 int reset,
                                 int brkIrq)
  {
    this.coProcessorEnable = coProcessorEnable;
    this.unused = unused;
    this.abort = abort;
    this.nonMaskableInterrupt = nonMaskableInterrupt;
    this.reset = reset;
    this.brkIrq = brkIrq;
  }
}

