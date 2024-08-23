" return 200 when the file is successfully deleted " in {
      // Mock the gitService.deleteDirectoryOrFile method to return a successful result
      val userName = "user"
      val repoName = "repo"
      val path = "path"
      val fileName = "file.txt"

      val formData = DeleteModel("Commit message", "someSha")
      when(mockGitHubServices.deleteDirectoryOrFile(any(), any(), any(), eqTo(formData))(any()))
        .thenReturn(EitherT.rightT("File deleted"))
      // Simulate a valid form submission
      implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest(GET, s"/GitHub/repos/$userName/$repoName/$path/$fileName").withFormUrlEncodedBody("message" -> "Commit message", "sha" -> "someSha")
      val boundForm = deleteForm.bindFromRequest()
      boundForm.hasErrors mustBe false
      val result: Future[Result] = testGitHubRepoController.deleteDirectoryOrFile(userName, repoName,path, fileName)(request)

      // Assert the status and content of the response
      status(result) mustBe CREATED

    }