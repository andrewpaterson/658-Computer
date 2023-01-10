package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.trace.TraceData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.Trace;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.integratedcircuit.PassiveView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.trace.TraceView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<TraceView> traceViews;
  protected Circuit circuit;
  protected Simulation simulation;
  protected List<View> selection;

  public CircuitEditor()
  {
    this.circuit = new Circuit();
    this.simulation = circuit.resetSimulation();
    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.passiveViews = new LinkedHashSet<>();
    this.selection = new ArrayList<>();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    long time = getTime();
    List<TraceView> traceViews;
    List<IntegratedCircuitView<?, ?>> semiconductorViews;
    List<PassiveView<?, ?>> passiveViews;
    synchronized (this)
    {
      traceViews = new ArrayList<>(this.traceViews);
      semiconductorViews = new ArrayList<>(this.integratedCircuitViews);
      passiveViews = new ArrayList<>(this.passiveViews);
    }

    for (TraceView traceView : traceViews)
    {
      traceView.paint(graphics, viewport, time);
    }

    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      passiveView.paint(graphics, viewport, time);
    }

    for (IntegratedCircuitView<?, ?> integratedCircuitView : semiconductorViews)
    {
      integratedCircuitView.paint(graphics, viewport, time);
    }
  }

  protected long getTime()
  {
    return simulation.getTime();
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void deleteComponentView(ComponentView<?> componentView)
  {
    if (componentView instanceof IntegratedCircuitView)
    {
      Set<PortView> updatedPortViews = deleteIntegratedCircuit((IntegratedCircuitView<?, ?>) componentView);
      fireConnectionEvents(updatedPortViews);
    }
    else if (componentView instanceof PassiveView)
    {
      Set<PortView> updatedPortViews = deletePassiveView((PassiveView<?, ?>) componentView);
      fireConnectionEvents(updatedPortViews);
    }
    else if (componentView == null)
    {
      throw new SimulatorException("Cannot delete null view.");
    }
    else
    {
      throw new SimulatorException("Cannot delete view of class [%s].", componentView.getClass().getSimpleName());
    }

    validateConsistency();
  }

  protected Set<PortView> deleteIntegratedCircuit(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    List<ConnectionView> connectionViews = disconnectComponentView(integratedCircuitView);

    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    circuit.remove(integratedCircuit);
    removeIntegratedCircuitView(integratedCircuitView);

    return connectConnections(connectionViews);
  }

  protected Set<PortView> deletePassiveView(PassiveView<?, ?> passiveView)
  {
    List<ConnectionView> connectionViews = disconnectComponentView(passiveView);

    Passive powerSource = passiveView.getComponent();
    circuit.remove(powerSource);
    removePassiveView(passiveView);

    return connectConnections(connectionViews);
  }

  public List<ConnectionView> disconnectComponentView(ComponentView<?> componentView)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    List<PortView> portViews = componentView.getPorts();
    for (PortView portView : portViews)
    {
      ConnectionView connection = portView.getConnection();
      if (connection != null)
      {
        Set<ConnectionView> connectionsFromConnection = findConnections(connection);
        disconnectConnectionViews(connectionsFromConnection);

        connection.remove(componentView);
        connectionViews.add(connection);
      }
    }

    circuit.disconnectDiscrete(componentView.getComponent(), simulation);

    return connectionViews;
  }

  public Set<PortView> connectConnections(List<ConnectionView> connectionViews)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      updatedPortViews.addAll(connectNewConnections(connectionView));
    }

    return updatedPortViews;
  }

  public boolean deleteTraceViews(ConnectionView connectionView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    List<View> connectedComponents = new ArrayList<>(connectionView.getConnectedComponents());
    boolean traceDeleted = false;
    for (View view : connectedComponents)
    {
      if (view instanceof TraceView)
      {
        traceDeleted = true;
        Set<PortView> localUpdatedPortViews = deleteTraceView((TraceView) view);
        updatedPortViews.addAll(localUpdatedPortViews);
      }
    }

    if (traceDeleted)
    {
      fireConnectionEvents(updatedPortViews);
      validateConsistency();
    }

    return traceDeleted;
  }

  protected void deleteTraceView(ConnectionView connectionView, TraceView traceView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    if (!connectionView.isConcrete())
    {
      updatedPortViews = deleteTraceView(traceView);
    }
    else
    {
      List<View> connectedComponents = new ArrayList<>(connectionView.getConnectedComponents());
      for (View view : connectedComponents)
      {
        if (view instanceof TraceView)
        {
          TraceView concreteTraceView1 = (TraceView) view;
          updatedPortViews.addAll(deleteTraceView(concreteTraceView1));
        }
      }
    }

    fireConnectionEvents(updatedPortViews);
    validateConsistency();
  }

  public void deleteTraceViewInternal(TraceView traceView)
  {
    disconnectTraceView(traceView);
    traceView.removed();
    removeTraceView(traceView);
  }

  protected List<ConnectionView> disconnectTraceView(TraceView traceView)
  {
    if (traceView.isRemoved())
    {
      throw new SimulatorException("Cannot disconnect a removed trace.");
    }

    ConnectionView startConnection = traceView.getStartConnection();
    ConnectionView endConnection = traceView.getEndConnection();

    disconnectConnectionViews(findConnections(startConnection));
    disconnectConnectionViews(findConnections(endConnection));

    startConnection.remove(traceView);
    endConnection.remove(traceView);

    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    connectionViews.add(startConnection);
    connectionViews.add(endConnection);
    return connectionViews;
  }

  public void disconnectConnectionViews(Set<ConnectionView> connectionsNet)
  {
    for (ConnectionView connection : connectionsNet)
    {
      List<View> connectedComponents = connection.getConnectedComponents();
      for (View connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          ((TraceView) connectedComponent).disconnectTraceNet();
        }
        else if (connectedComponent instanceof IntegratedCircuitView)
        {
          IntegratedCircuitView<?, ?> integratedCircuitView = (IntegratedCircuitView<?, ?>) connectedComponent;
          Int2D position = connection.getGridPosition();
          if (position != null)
          {
            PortView portView = integratedCircuitView.getPortInGrid(position);
            portView.disconnect(simulation);
          }
        }
      }
    }
  }

  public Set<PortView> connectNewConnections(ConnectionView connectionView)
  {
    Set<ConnectionView> connectionsNet = findConnections(connectionView);
    return connectNewTraceNet(connectionsNet);
  }

  public Simulation reset()
  {
    simulation = circuit.resetSimulation();
    return simulation;
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }

  public ComponentView<?> getComponentViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<ComponentView<?>> selectedViews = getComponentViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      ComponentView<?> closestView = null;
      for (ComponentView<?> view : selectedViews)
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
        float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<ComponentView<?>> getComponentViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    Int2D boundBoxPosition = new Int2D();
    Int2D boundBoxDimension = new Int2D();
    List<ComponentView<?>> selectedViews = new ArrayList<>();
    for (IntegratedCircuitView<?, ?> view : integratedCircuitViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        if (BoundingBox.containsPoint(screenPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    for (PassiveView<?, ?> view : passiveViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        if (BoundingBox.containsPoint(screenPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<ComponentView<?>> getComponentViewsInGridSpace(Int2D gridPosition)
  {
    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<ComponentView<?>> selectedViews = new ArrayList<>();
    for (ComponentView<?> view : integratedCircuitViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.containsPoint(gridPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    for (PassiveView<?, ?> view : passiveViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.containsPoint(gridPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<TraceView> getTraceViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    int x = viewport.transformScreenToGridX(screenPosition.x);
    int y = viewport.transformScreenToGridY(screenPosition.y);
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : this.traceViews)
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public TraceView getTraceViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<TraceView> selectedViews = getTraceViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D center = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      TraceView closestView = null;
      for (TraceView view : selectedViews)
      {
        view.getCenter(center);
        float distance = BoundingBox.calculateDistance(screenPosition, center);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<TraceView> getTraceViewsInGridSpace(Int2D position)
  {
    return getTraceViewsInGridSpace(position.x, position.y);
  }

  public List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> result = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      if (traceView.contains(x, y))
      {
        result.add(traceView);
      }
    }
    return result;
  }

  public List<TraceOverlap> getTracesOverlapping(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      LineOverlap overlap = traceView.getOverlap(line);
      if (overlap != null)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public ConnectionView getConnection(Int2D position)
  {
    List<ComponentView<?>> componentViews = getComponentViewsInGridSpace(position);
    List<TraceView> traceViews = getTraceViewsInGridSpace(position);
    Set<ConnectionView> connectionViews = new HashSet<>();

    for (TraceView traceView : traceViews)
    {
      ConnectionView connectionView = traceView.getConnectionsInGrid(position);
      if (connectionView != null)
      {
        connectionViews.add(connectionView);
      }
    }

    for (ComponentView<?> componentView : componentViews)
    {
      PortView portView = componentView.getPortInGrid(position.x, position.y);
      if (portView != null)
      {
        connectionViews.add(portView.getConnection());
      }
    }

    if (connectionViews.size() == 1)
    {
      return connectionViews.iterator().next();
    }
    else if (connectionViews.size() == 0)
    {
      return null;
    }
    else
    {
      throw new SimulatorException("Expected all connections at location [" + position.toString() + "] to be the same.");
    }
  }

  public ConnectionView getOrAddConnection(Int2D position, View view)
  {
    ConnectionView connection = getConnection(position);
    if (connection != null)
    {
      connection.add(view);
      return connection;
    }
    else
    {
      return new ConnectionView(view);
    }
  }

  public Set<ConnectionView> findConnections(ConnectionView connection)
  {
    if (connection != null)
    {
      ArrayList<ConnectionView> connectionsToProcess = new ArrayList<>();
      connectionsToProcess.add(connection);
      Set<ConnectionView> connectionsNet = new LinkedHashSet<>();
      while (connectionsToProcess.size() > 0)
      {
        ConnectionView currentConnection = connectionsToProcess.get(0);
        connectionsToProcess.remove(0);
        connectionsNet.add(currentConnection);

        List<View> components = currentConnection.getConnectedComponents();
        if (components != null)
        {
          for (View component : components)
          {
            List<ConnectionView> connectedConnections = component.getConnectedConnections(currentConnection);
            for (ConnectionView connectedConnection : connectedConnections)
            {
              if (!connectionsNet.contains(connectedConnection))
              {
                connectionsToProcess.add(connectedConnection);
              }
            }
          }
        }
      }
      return connectionsNet;
    }
    else
    {
      return new LinkedHashSet<>();
    }
  }

  public Set<PortView> connectNewTraceNet(Set<ConnectionView> connectionsNet)
  {
    Set<PortView> connectedPorts = new LinkedHashSet<>();
    Set<TraceView> connectedTraceViews = new LinkedHashSet<>();
    int numberOfTraces = 0;
    for (ConnectionView connection : connectionsNet)
    {
      List<View> connectedComponents = connection.getConnectedComponents();
      for (View connectedComponent : connectedComponents)
      {
        Int2D position = connection.getGridPosition();
        if (position != null)
        {
          if (connectedComponent instanceof ComponentView)
          {
            ComponentView<?> integratedCircuitView = (ComponentView<?>) connectedComponent;
            PortView portView = integratedCircuitView.getPortInGrid(position);
            if (portView.numberOfPorts() > numberOfTraces)
            {
              numberOfTraces = portView.numberOfPorts();
            }
            connectedPorts.add(portView);
          }
          else if (connectedComponent instanceof TraceView)
          {
            connectedTraceViews.add((TraceView) connectedComponent);
          }
        }
      }
    }

    List<Trace> traces = new ArrayList<>(numberOfTraces);
    for (int i = 0; i < numberOfTraces; i++)
    {
      traces.add(new Trace());
    }

    for (PortView connectedPortView : connectedPorts)
    {
      connectedPortView.connectTraceNet(traces, simulation);
    }
    for (TraceView connectedTraceView : connectedTraceViews)
    {
      connectedTraceView.connectTraceNet(traces);
    }
    return connectedPorts;
  }

  public TraceView mergeTrace(TraceView traceView, boolean forDelete)
  {
    Rotation direction = traceView.getDirection();
    ConnectionView startConnection = traceView.getStartConnection();
    ConnectionView endConnection = traceView.getEndConnection();

    List<TraceView> towardsEnd = findStraightTracesInDirection(traceView, direction, endConnection);
    List<TraceView> towardsStart = findStraightTracesInDirection(traceView, direction.opposite(), startConnection);
    if (towardsEnd.size() > 0 | (towardsStart.size() > 0))
    {
      List<TraceView> mergeViews = new ArrayList<>(towardsStart);
      mergeViews.add(traceView);
      mergeViews.addAll(towardsEnd);
      Int2D smallest = new Int2D(traceView.getMinimumX(), traceView.getMinimumY());
      Int2D largest = new Int2D(traceView.getMaximumX(), traceView.getMaximumY());
      for (TraceView mergeView : mergeViews)
      {
        int x1 = mergeView.getMinimumX();
        if (x1 < smallest.x)
        {
          smallest.x = x1;
        }
        int y1 = mergeView.getMinimumY();
        if (y1 < smallest.y)
        {
          smallest.y = y1;
        }
        int x2 = mergeView.getMaximumX();
        if (x2 > largest.x)
        {
          largest.x = x2;
        }
        int y2 = mergeView.getMaximumY();
        if (y2 > largest.y)
        {
          largest.y = y2;
        }
        deleteTraceViewInternal(mergeView);
      }

      if (isValidTrace(smallest, largest))
      {
        if (!forDelete)
        {
          return new TraceView(this, smallest, largest);
        }
        else
        {
          return null;
        }
      }
      else
      {
        throw new SimulatorException("Invalid trace created from merged traces.");
      }
    }
    else
    {
      return traceView;
    }
  }

  private boolean isValidTrace(Int2D start, Int2D end)
  {
    if ((start != null) && (end != null))
    {
      if (((start.x == end.x) || (start.y == end.y)) &&
          !((start.x == end.x) && (start.y == end.y)))
      {
        return true;
      }
    }
    return false;
  }

  private List<TraceView> findStraightTracesInDirection(TraceView traceView, Rotation direction, ConnectionView endConnection)
  {
    ArrayList<TraceView> result = new ArrayList<>();

    TraceView currentTrace = traceView;
    ConnectionView currentConnection = endConnection;
    for (; ; )
    {
      List<View> connectedComponents = currentConnection.getConnectedComponents();
      TraceView otherTrace = currentTrace.getOtherTraceView(connectedComponents);

      if (otherTrace != null)
      {
        Rotation otherDirection = otherTrace.getDirection();
        if (direction.isStraight(otherDirection))
        {
          result.add(otherTrace);
          currentConnection = otherTrace.getOpposite(currentConnection);
          currentTrace = otherTrace;
        }
        else
        {
          break;
        }
      }
      else
      {
        break;
      }
    }

    return result;
  }

  private List<TraceView> splitTrace(Int2D position, Rotation direction)
  {
    List<TraceView> result = new ArrayList<>();
    List<TraceView> traceViews = getTraceViewsInGridSpace(position);
    for (TraceView traceView : traceViews)
    {
      ConnectionView connectionView = traceView.getPotentialConnectionsInGrid(position);
      if (connectionView instanceof HoverConnectionView)
      {
        if (!direction.isStraight(traceView.getDirection()))
        {
          Int2D startPosition = traceView.getStartPosition();
          Int2D endPosition = traceView.getEndPosition();
          deleteTraceViewInternal(traceView);

          result.add(new TraceView(this, startPosition, position));
          result.add(new TraceView(this, position, endPosition));
        }
      }
    }
    return result;
  }

  public Set<PortView> deleteTraceView(TraceView traceView)
  {
    if (!traceView.isRemoved())
    {
      ConnectionView startConnection = traceView.getStartConnection();
      ConnectionView endConnection = traceView.getEndConnection();

      deleteTraceViewInternal(traceView);

      Set<PortView> portViews = mergeAndConnectAfterDelete(startConnection);
      portViews.addAll(mergeAndConnectAfterDelete(endConnection));
      return portViews;
    }
    return new LinkedHashSet<>();
  }

  protected Set<PortView> mergeAndConnectAfterDelete(ConnectionView startConnection)
  {
    TraceView traceView = mergeTraceConnectionForDelete(startConnection);
    ConnectionView connection;
    if (traceView != null)
    {
      connection = traceView.getStartConnection();
    }
    else
    {
      connection = startConnection;
    }
    return connectNewConnections(connection);
  }

  private TraceView mergeTraceConnectionForDelete(ConnectionView startConnection)
  {
    List<View> connectedComponents = startConnection.getConnectedComponents();
    TraceView traceView1 = null;
    TraceView traceView2 = null;
    if (connectedComponents.size() == 2)
    {
      for (View connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          if (traceView1 == null)
          {
            traceView1 = (TraceView) connectedComponent;
          }
          else
          {
            traceView2 = (TraceView) connectedComponent;
          }
        }
      }
    }

    if ((traceView1 != null) && (traceView2 != null))
    {
      return mergeTrace(traceView1, true);
    }
    return null;
  }

  public Set<TraceView> createTraceViews(Line line)
  {
    List<TraceView> editedTraceViews = new ArrayList<>();

    editedTraceViews.addAll(splitTrace(line.getStart(), line.getDirection()));
    editedTraceViews.addAll(splitTrace(line.getEnd(), line.getDirection()));

    List<ConnectionView> sortedConnections = new ArrayList<>(getConnectionsOnLine(line));

    Collections.sort(sortedConnections);

    if (sortedConnections.size() == 0)
    {
      editedTraceViews.add(new TraceView(this, line.getStart(), line.getEnd()));
    }
    else
    {
      if (line.isEastWest())
      {
        List<Int2D> positions = getEastWestPositions(line, sortedConnections);
        editedTraceViews.addAll(createTracesBetweenEmptyPositions(positions));
      }
      else if (line.isNorthSouth())
      {
        List<Int2D> positions = getNorthSouthPositions(line, sortedConnections);
        editedTraceViews.addAll(createTracesBetweenEmptyPositions(positions));
      }
    }

    Set<TraceView> mergedTraces = new LinkedHashSet<>();
    if (!editedTraceViews.isEmpty())
    {
      for (TraceView traceView : editedTraceViews)
      {
        TraceView mergedTrace = mergeTrace(traceView, false);
        if (!mergedTrace.isRemoved())
        {
          mergedTraces.add(mergedTrace);
        }
      }
    }
    return mergedTraces;
  }

  private List<TraceView> createTracesBetweenEmptyPositions(List<Int2D> positions)
  {
    List<TraceView> result = new ArrayList<>();
    for (int i = 0; i < positions.size() - 1; i++)
    {
      Int2D start = positions.get(i);
      Int2D end = positions.get(i + 1);
      List<TraceOverlap> tracesOverlapping = getTracesOverlapping(new Line(start, end));
      if (tracesOverlapping.size() == 0)
      {
        TraceView traceView = new TraceView(this, start, end);
        result.add(traceView);
      }
    }
    return result;
  }

  private List<Int2D> getEastWestPositions(Line line, List<ConnectionView> sortedConnections)
  {
    int minimumX = line.getMinimumX();
    int maximumX = line.getMaximumX();
    int y = line.getY();

    Int2D start = new Int2D(minimumX, y);
    Int2D end = new Int2D(maximumX, y);
    return getPositions(sortedConnections, start, end);
  }

  private List<Int2D> getNorthSouthPositions(Line line, List<ConnectionView> sortedConnections)
  {
    int minimumY = line.getMinimumY();
    int maximumY = line.getMaximumY();
    int x = line.getX();

    Int2D start = new Int2D(x, minimumY);
    Int2D end = new Int2D(x, maximumY);
    return getPositions(sortedConnections, start, end);
  }

  private List<Int2D> getPositions(List<ConnectionView> sortedConnections, Int2D start, Int2D end)
  {
    List<Int2D> positions = new ArrayList<>();

    ConnectionView firstConnection = sortedConnections.get(0);
    if (!firstConnection.getGridPosition().equals(start))
    {
      positions.add(start);
    }

    for (ConnectionView sortedConnection : sortedConnections)
    {
      positions.add(sortedConnection.getGridPosition());
    }

    Int2D lastPosition = positions.get(positions.size() - 1);
    if (!end.equals(lastPosition))
    {
      positions.add(end);
    }
    return positions;
  }

  private Set<ConnectionView> getConnectionsOnLine(Line line)
  {
    Set<ConnectionView> notStraightConnections = new LinkedHashSet<>();
    notStraightConnections.addAll(getDiscretePortConnectionsOnLine(line));
    notStraightConnections.addAll(getTraceConnectionsOnLine(line));
    return notStraightConnections;
  }

  private Set<ConnectionView> getDiscretePortConnectionsOnLine(Line line)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
    {
      List<PortView> portViews = integratedCircuitView.getPorts();
      for (PortView portView : portViews)
      {
        ConnectionView connection = portView.getConnection();
        if (line.isPositionOn(connection.getGridPosition()))
        {
          connectionViews.add(connection);
        }
      }
    }

    return connectionViews;
  }

  private Set<ConnectionView> getTraceConnectionsOnLine(Line line)
  {
    Set<ConnectionView> notStraightConnections = new LinkedHashSet<>();
    for (TraceView traceView : traceViews)
    {
      if (line.isPositionOn(traceView.getStartPosition()))
      {
        notStraightConnections.add(traceView.getStartConnection());
      }
      if (line.isPositionOn(traceView.getEndPosition()))
      {
        notStraightConnections.add(traceView.getEndConnection());
      }
    }
    return notStraightConnections;
  }

  public void validateConsistency()
  {
    for (TraceView traceView : traceViews)
    {
      if (!traceView.isRemoved())
      {
        ConnectionView startConnection = traceView.getStartConnection();
        for (View view : startConnection.getConnectedComponents())
        {
          boolean contained = true;
          if (view instanceof IntegratedCircuitView)
          {
            if (!integratedCircuitViews.contains(view))
            {
              contained = false;
            }
          }

          if (view instanceof TraceView)
          {
            if (!traceViews.contains(view))
            {
              contained = false;
            }
          }

          if (view instanceof PassiveView)
          {
            if (!passiveViews.contains(view))
            {
              contained = false;
            }
          }

          if (!contained)
          {
            throw new SimulatorException(view.getDescription() + " referenced by trace [" + traceView.getDescription() + "] does not include trace.");
          }
        }
      }
    }
  }

  public CircuitData save()
  {
    Set<View> selection = new HashSet<>(this.selection);
    ArrayList<IntegratedCircuitData<?, ?>> semiconductorDatas = new ArrayList<>();
    for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
    {
      IntegratedCircuitData<?, ?> integratedCircuitData = integratedCircuitView.save(selection.contains(integratedCircuitView));
      if (integratedCircuitData == null)
      {
        throw new SimulatorException("%s [%s] save may not return null.", integratedCircuitView.getClass().getSimpleName(), integratedCircuitView.getName());
      }

      semiconductorDatas.add(integratedCircuitData);
    }

    ArrayList<PassiveData> passiveDatas = new ArrayList<>();
    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      PassiveData passiveData = passiveView.save(selection.contains(passiveView));
      if (passiveData == null)
      {
        throw new SimulatorException("%s [%s] save may not return null.", passiveView.getClass().getSimpleName(), passiveView.getName());
      }
      passiveDatas.add(passiveData);
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    TimelineData timelineData = simulation.getTimeline().save();
    return new CircuitData(timelineData,
                           semiconductorDatas,
                           passiveDatas,
                           traceDatas);
  }

  public void load(CircuitData circuitData)
  {
    clearSelection();
    simulation.getTimeline().load(circuitData.timeline);

    TraceLoader traceLoader = new TraceLoader();

    for (TraceData traceData : circuitData.traces)
    {
      traceData.create(this, traceLoader);
    }

    for (PassiveData passiveData : circuitData.passives)
    {
      passiveData.createAndLoad(this, traceLoader);
    }

    for (IntegratedCircuitData<?, ?> integratedCircuitData : circuitData.integratedCircuits)
    {
      integratedCircuitData.createAndLoad(this, traceLoader);
    }
  }

  public Set<PortView> createAndConnectComponentView(ComponentView<?> componentView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    List<PortView> ports = componentView.getPorts();
    List<ConnectionView> newConnections = new ArrayList<>();
    for (PortView portView : ports)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = getOrAddConnection(portPosition, componentView);
      portView.setConnection(connectionView);
      newConnections.add(connectionView);
    }

    for (ConnectionView connectionView : newConnections)
    {
      Set<PortView> portViews = connectNewConnections(connectionView);
      updatedPortViews.addAll(portViews);
    }

    componentView.enable(simulation);
    componentView.simulationStarted(simulation);

    return updatedPortViews;
  }

  public void createConnectionViews(ComponentView<?> componentView)
  {
    List<PortView> ports = componentView.getPorts();
    for (PortView portView : ports)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = getOrAddConnection(portPosition, componentView);
      portView.setConnection(connectionView);
    }
    componentView.enable(simulation);
  }

  public void fireConnectionEvents(Set<PortView> updatedPortViews)
  {
    for (PortView portView : updatedPortViews)
    {
      portView.traceConnected(simulation);
    }
  }

  public Timeline getTimeline()
  {
    return simulation.getTimeline();
  }

  public Simulation getSimulation()
  {
    return simulation;
  }

  public void placeComponentView(ComponentView<?> componentView)
  {
    Set<PortView> updatedPortViews = createAndConnectComponentView(componentView);

    fireConnectionEvents(updatedPortViews);
    validateConsistency();
  }

  public void startMoveComponents(List<View> views)
  {
    for (View view : views)
    {
      if (view instanceof IntegratedCircuitView)
      {
        IntegratedCircuitView<?, ?> integratedCircuitView = (IntegratedCircuitView<?, ?>) view;
        List<ConnectionView> connectionViews = disconnectComponentView(integratedCircuitView);

        connectConnections(connectionViews);
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        disconnectTraceView(traceView);
        traceView.removed();
      }
    }
    validateConsistency();

    clearSelection();
  }

  public void updateSelection(SelectionRectangle selectionRectangle)
  {
    selection = getSelection(selectionRectangle.start, selectionRectangle.end);
  }

  public void doneMoveComponents(List<View> views)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();

    List<Line> lines = new ArrayList<>();

    for (View view : views)
    {
      if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        removeTraceView(traceView);
        lines.add(traceView.getLine());
      }
    }

    List<View> selection = new ArrayList<>();
    for (View view : views)
    {
      if ((view instanceof IntegratedCircuitView) ||
          (view instanceof PassiveView))
      {
        ComponentView<?> componentView = (ComponentView<?>) view;
        updatedPortViews.addAll(createAndConnectComponentView(componentView));
        selection.add(componentView);
      }
    }

    for (Line line : lines)
    {
      Set<TraceView> newTraces = createTraceViews(line);
      finaliseCreatedTraces(newTraces);
      selection.addAll(newTraces);
    }

    fireConnectionEvents(updatedPortViews);
    validateConsistency();

    List<View> cleanSelection = new ArrayList<>();
    for (View view : selection)
    {
      if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        if (!traceView.isRemoved())
        {
          cleanSelection.add(view);
        }
      }
      else
      {
        cleanSelection.add(view);
      }
    }

    this.selection = cleanSelection;
  }

  public List<View> getSelection()
  {
    return selection;
  }

  public void clearSelection()
  {
    if (!selection.isEmpty())
    {
      selection = new ArrayList<>();
    }
  }

  public boolean isSelectionEmpty()
  {
    return selection.isEmpty();
  }

  public List<View> getSelection(Float2D start, Float2D end)
  {
    boolean includeIntersections = start.x > end.x;

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<View> selectedViews = new ArrayList<>();
    for (ComponentView<?> componentView : integratedCircuitViews)
    {
      updateSelectedViews(start, end, includeIntersections, boundBoxPosition, boundBoxDimension, selectedViews, componentView);
    }

    for (ComponentView<?> componentView : passiveViews)
    {
      updateSelectedViews(start, end, includeIntersections, boundBoxPosition, boundBoxDimension, selectedViews, componentView);
    }

    for (TraceView traceView : traceViews)
    {
      Line line = traceView.getLine();
      line.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (line.isPositionOn(new Int2D(start)))
        {
          selectedViews.add(traceView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(traceView);
        }
      }
    }
    return selectedViews;
  }

  protected void updateSelectedViews(Float2D start,
                                     Float2D end,
                                     boolean includeIntersections,
                                     Float2D boundBoxPosition,
                                     Float2D boundBoxDimension,
                                     List<View> selectedViews,
                                     ComponentView<?> componentView)
  {
    if (componentView.isEnabled())
    {
      componentView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (BoundingBox.containsPoint(new Int2D(start), boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(componentView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(componentView);
        }
      }
    }
  }

  protected boolean isPoint(Float2D start, Float2D end)
  {
    return (Math.round(start.x) == Math.round(end.x)) &&
           (Math.round(start.y) == Math.round(end.y));
  }

  public void deleteComponent(View view)
  {
    if (view != null)
    {
      if (view instanceof TraceView)
      {
        deleteTraceView((TraceView) view);
      }
      else if (view instanceof ComponentView<?>)
      {
        deleteComponentView((ComponentView<?>) view);
      }
      else
      {
        throw new SimulatorException("Don't know how to delete component [%s].", view.getClass().getSimpleName());
      }
    }
  }

  protected void finaliseCreatedTraces(Set<TraceView> traceViews)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    for (TraceView traceView : traceViews)
    {
      if (traceView.isRemoved())
      {
        throw new SimulatorException("Cannot finalise a removed Trace.");
      }
      updatedPortViews.addAll(connectNewConnections(traceView.getStartConnection()));
    }

    fireConnectionEvents(updatedPortViews);
    validateConsistency();
  }

  public void deleteSelection()
  {
    for (View view : selection)
    {
      deleteComponent(view);
    }
    clearSelection();
  }

  public IntegratedCircuitView<?, ?> getSingleSelectionDiscreteView()
  {
    IntegratedCircuitView<?, ?> singleSemiconductorView = null;
    for (View view : selection)
    {
      if (view instanceof IntegratedCircuitView)
      {
        if (singleSemiconductorView == null)
        {
          singleSemiconductorView = (IntegratedCircuitView<?, ?>) view;
        }
        else
        {
          return null;
        }
      }
    }
    return singleSemiconductorView;
  }

  public void select(View view)
  {
    selection.add(view);
  }

  public void replaceSelection(View newView, View oldView)
  {
    int i = selection.indexOf(oldView);
    if (i != -1)
    {
      selection.set(i, newView);
    }
  }

  public void addSemiconductorView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.add(integratedCircuitView);
    }
  }

  public void removeIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.remove(integratedCircuitView);
    }
  }

  public void addPassiveView(PassiveView<?, ?> view)
  {
    synchronized (this)
    {
      passiveViews.add(view);
    }
  }

  protected boolean removePassiveView(PassiveView<?, ?> passiveView)
  {
    synchronized (this)
    {
      return passiveViews.remove(passiveView);
    }
  }

  public void addTraceView(TraceView view)
  {
    synchronized (this)
    {
      traceViews.add(view);
    }
  }

  protected boolean removeTraceView(TraceView traceView)
  {
    synchronized (this)
    {
      return traceViews.remove(traceView);
    }
  }
}

