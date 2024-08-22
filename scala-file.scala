connector.delete[JsValue](url, body)(Reads.JsValueReads, ex).leftMap {
    case APIError.BadAPIResponse(code, msg) => APIError.BadAPIResponse(code, msg)
  }.subflatMap { json =>
    json.asOpt[JsObject] match {
      case Some(item) if (item \ "status").asOpt[String].contains("404") =>
        Left(APIError.NotFoundError(404, "User not found in Github"))

      case Some(item) if (item \ "message").asOpt[String].contains(formData.message) &&
                         (item \ "sha").asOpt[String].contains(formData.sha) =>
        // JSON contains the expected 'message' and 'sha', return it as a string
        Right(item.toString())

      case Some(item) if !(item \ "message").asOpt[String].contains(formData.message) &&
                         !(item \ "sha").asOpt[String].contains(formData.sha) =>
        // JSON does not contain the expected 'message' and 'sha', return error
        Left(APIError.BadAPIResponse(500, "Error with Github Response Data"))

      case None =>
        // JSON is not valid
        Left(APIError.BadAPIResponse(500, "Error with Github Response Data"))
    }
  }
}