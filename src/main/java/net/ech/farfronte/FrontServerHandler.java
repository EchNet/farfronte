package net.ech.farfronte;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class FrontServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(final ChannelHandlerContext ctx) {
    super.channelActive(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    try {
      ctx.write(msg);
      ctx.flush();
    }
    finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}
