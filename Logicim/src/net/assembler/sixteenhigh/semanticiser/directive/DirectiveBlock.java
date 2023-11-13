package net.assembler.sixteenhigh.semanticiser.directive;

public class DirectiveBlock
{
  public long startAddress;
  public long endAddress;
  public AccessMode accessMode;
  public int accessTime;

  public DirectiveBlock()
  {
    startAddress = 0;
    endAddress = Long.MAX_VALUE;
    accessTime = 1;
    accessMode = AccessMode.READ_WRITE;
  }

  public DirectiveBlock(DirectiveBlock directiveBlock)
  {
    startAddress = directiveBlock.startAddress;
    endAddress = directiveBlock.endAddress;
    accessTime = directiveBlock.accessTime;
    accessMode = directiveBlock.accessMode;
  }
}

