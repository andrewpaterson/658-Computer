package name.bizna.emu65816;

public class NativeModeInterrupts
{
  public int coProcessorEnable;
  public int brk;
  public int abort;
  public int nonMaskableInterrupt;
  public int reset;
  public int interruptRequest;

  public NativeModeInterrupts(int coProcessorEnable,
                              int brk,
                              int abort,
                              int nonMaskableInterrupt,
                              int reset,
                              int interruptRequest)
  {
    this.coProcessorEnable = coProcessorEnable;
    this.brk = brk;
    this.abort = abort;
    this.nonMaskableInterrupt = nonMaskableInterrupt;
    this.reset = reset;
    this.interruptRequest = interruptRequest;
  }
}

