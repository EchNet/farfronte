package net.ech.farfronte;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Handles a server-side channel.
 */
public class FrontServerHandler extends ChannelInboundHandlerAdapter {

  private HttpRequest httpRequest;

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof HttpRequest) {
      httpRequest = (HttpRequest) msg;
    }
    else if (msg instanceof LastHttpContent) {
      writeResponse(OK, "HttpRequest", ctx);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
    ctx.close();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }

  private boolean writeResponse(HttpResponseStatus status, String msg, ChannelHandlerContext ctx) {
    FullHttpResponse response = new DefaultFullHttpResponse(
        HTTP_1_1, status, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));

    response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

      // Write the response.
    ctx.write(response);
    return false;  // keepalive
  }
}
