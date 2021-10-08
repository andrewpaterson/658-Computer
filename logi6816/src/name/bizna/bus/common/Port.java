package name.bizna.bus.common;

public class Port
{
  protected TransmissionState state;
  protected TraceState value;

  public Port()
  {
    this.value = TraceState.Undefined;
    this.state = TransmissionState.Impedance;
  }

  public boolean get()
  {
    return value == TraceState.High;
  }

  public TraceState getState()
  {
    return value;
  }

  public void set(TraceState value)
  {
    if (state == TransmissionState.Output)
    {
      this.value = value;
    }
    else
    {
      this.value = TraceState.Error;
    }
  }

  public void set(boolean value)
  {
    set(value ? TraceState.High : TraceState.Low);
  }
}

