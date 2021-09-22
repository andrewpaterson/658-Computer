package name.bizna.emu65816;

public class EmulationModeInterrupts
{
  public short coProcessorEnable;
  public short unused;
  public short abort;
  public short nonMaskableInterrupt;
  public short reset;
  public short brkIrq;

  public EmulationModeInterrupts(short coProcessorEnable,
                                 short unused,
                                 short abort,
                                 short nonMaskableInterrupt,
                                 short reset,
                                 short brkIrq)
  {
    this.coProcessorEnable = coProcessorEnable;
    this.unused = unused;
    this.abort = abort;
    this.nonMaskableInterrupt = nonMaskableInterrupt;
    this.reset = reset;
    this.brkIrq = brkIrq;
  }
}

