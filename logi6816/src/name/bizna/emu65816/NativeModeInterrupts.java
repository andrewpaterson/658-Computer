package name.bizna.emu65816;

public class NativeModeInterrupts
{
  public short coProcessorEnable;
  public short brk;
  public short abort;
  public short nonMaskableInterrupt;
  public short reset;
  public short interruptRequest;

  public NativeModeInterrupts(short coProcessorEnable,
                              short brk,
                              short abort,
                              short nonMaskableInterrupt,
                              short reset,
                              short interruptRequest)
  {
    this.coProcessorEnable = coProcessorEnable;
    this.brk = brk;
    this.abort = abort;
    this.nonMaskableInterrupt = nonMaskableInterrupt;
    this.reset = reset;
    this.interruptRequest = interruptRequest;
  }
}

