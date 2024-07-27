package net.assembler.sixteenhigh.semanticiser.directive;

public class Directive
{
  public long startAddress;
  public long endAddress;
  public AccessMode accessMode;
  public int accessTime;

  public Directive()
  {
    startAddress = 0;
    endAddress = Long.MAX_VALUE;
    accessTime = 1;
    accessMode = AccessMode.READ_WRITE;
  }

  public Directive(Directive directive)
  {
    startAddress = directive.startAddress;
    endAddress = directive.endAddress;
    accessTime = directive.accessTime;
    accessMode = directive.accessMode;
  }

  public void setStartAddress(long startAddress)
  {
    this.startAddress = startAddress;
  }

  public void setEndAddress(long endAddress)
  {
    this.endAddress = endAddress;
  }

  public void setAccessMode(AccessMode accessMode)
  {
    this.accessMode = accessMode;
  }

  public void setAccessTime(int accessTime)
  {
    this.accessTime = accessTime;
  }
}

