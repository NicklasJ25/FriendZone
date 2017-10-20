using FriendZoneAPI.Models;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Web.Http;

namespace FriendZoneAPI.Controllers
{
    public class CommentController : ApiController
    {
        FriendZoneEntities database = new FriendZoneEntities();

        // GET api/Comment
        public List<Comment> Get()
        {
            List<Comment> Comments = database.Comment.ToList();
            return Comments;
        }

        // GET api/Comment/id
        public Comment Get(int id)
        {
            Comment Comment = database.Comment.Find(id);
            return Comment;
        }

        // GET api/Comment/postID/start/count
        public List<Comment> Get(int postID, int start, int count)
        {
            List<Comment> Comments = database.Comment.Where(c => c.PostID.Equals(postID)).OrderByDescending(p => p.ID).ToList();
            Comments.RemoveRange(0, start);
            if (count < Comments.Count)
            {
                Comments.RemoveRange(count, Comments.Count);
            }
            return Comments;
        }

        // Comment api/Comment
        public void Comment([FromBody]Comment Comment)
        {
            database.Comment.Add(Comment);
            database.SaveChanges();
        }

        // PUT api/Comment/id
        public void Put(int id, [FromBody]Comment newComment)
        {
            Comment oldComment = database.Comment.Find(id);
            oldComment.Description = newComment.Description;
            database.SaveChanges();
        }

        // DELETE api/Comment/id
        public void Delete(int id)
        {
            database.Comment.Remove(database.Comment.SingleOrDefault(p => p.ID.Equals(id)));
            database.SaveChanges();
        }
    }
}
